package com.codecool.controller;

import com.codecool.models.Consumption;
import com.codecool.models.Inverter;
import com.codecool.models.LineItem;
import com.codecool.models.Offer;
import com.codecool.models.enums.CompanyEnum;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.models.forms.EmailForm;
import com.codecool.services.*;
import com.codecool.services.email.EmailService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
public class OfferController {

    private EmailService emailService;
    private OfferService offerService;
    private PdfService pdfService;
    private ValidationService validationService;
    private AdvertisingService advertisingService;
    private ConsumptionService consumptionService;

    private final String CONSUMPTION = "consumption";
    private final String DEVICE = "deviceForm";
    private final String EMAIL = "email";
    private final String STEP = "step";
    private final String OFFER = "offer";
    private final String PDF = "pdf";
    private final String METRIC = "metric";
    private final String SESSIONID = "sessionId";

    @Autowired
    public OfferController(OfferService offerService, PdfService pdfService,
                           EmailService emailService, ValidationService validationService,
                           AdvertisingService advertisingService, ConsumptionService consumptionService) {
        this.offerService = offerService;
        this.emailService = emailService;
        this.pdfService = pdfService;
        this.validationService = validationService;
        this.advertisingService = advertisingService;
        this.consumptionService = consumptionService;
    }

    @GetMapping("/ajanlat/1")
    public String getOfferStep1(@RequestParam(value = "key", required = false) String consumptionIDD, Model model) {
        Consumption consumption = new Consumption();
        model.addAttribute(METRIC, "Ft");
        if (null != consumptionIDD){
            model.addAttribute("consumptionId", consumptionIDD);
        }
        model.addAttribute(CONSUMPTION, consumption);
        model.addAttribute(STEP, '1');
        return "offer";
    }

    @PostMapping("/ajanlat/1")
    public String postOfferStep1(@RequestParam(value = "key", required = false) String consumptionIDD, @ModelAttribute ConsumptionForm consumptionForm) {
        Consumption consumption = new Consumption();

        if (consumptionService.getConsumptionByconsumptionID(consumptionIDD) == null) {
            consumption.setAdvertisement(consumptionForm.getAdvertisement());
        } else {
            consumption.setAlreadyGetOffer(true);
            consumption.setAdvertisement(consumption.getAdvertisement());
        }
        String consumptionID = UUID.randomUUID().toString().replace("-","");

        consumption.setConsumptionID(consumptionID);
        consumption.setPhase(consumptionForm.getPhase());
        consumption.setValue(consumptionForm.getValue());
        consumption.setAdvertisement(consumptionForm.getAdvertisement());
        consumption.setMetric(consumptionForm.getMetric());
        consumption.setCompany(CompanyEnum.TraditionalSolutions);

        consumptionService.saveConsuption(consumption);

        return "redirect:/ajanlat/2?key="+consumption.getConsumptionID();
    }
    // uricomponentbuilder
    @GetMapping("/ajanlat/2")
    public String getOfferStep2(@RequestParam(value = "key") String consumptionID, Model model) {
        Consumption consumption = consumptionService.getConsumptionByconsumptionID(consumptionID);

        if (consumptionService.getConsumptionByconsumptionID(consumptionID) == null) {
            log.info("Step1 is not done, redirecting to /ajanlat/1.");
            return "redirect:/ajanlat/1";
        }

        double calculatedConsumption = offerService.calculateConsumption(consumption);

        if (calculatedConsumption > 12000){
            log.info("The calculated consumption value is more than 12000: " + calculatedConsumption);
            log.info("Consumption detailes: " + consumption.toString());
            return "specialOfferNeeded";
        }

        DeviceForm pAndIForm = new DeviceForm();
        List<Inverter> inverterList = offerService.calculateInverterList(consumption);
        List<LineItem> solarPanelLineItems = offerService.getSolarPanelListAsLineItems(consumption);

        model.addAttribute("consumptionId", consumptionID);
        model.addAttribute(DEVICE, pAndIForm);
        model.addAttribute("solarPanelLineItems", solarPanelLineItems);
        model.addAttribute("inverterList", inverterList);
        model.addAttribute(STEP, '2');
        return "offer";
    }

    @PostMapping("/ajanlat/2")
    public String postOfferStep2(@RequestParam(value = "key") String consumptionID, @ModelAttribute DeviceForm deviceForm) {
        Consumption consumption = consumptionService.getConsumptionByconsumptionID(consumptionID);

        consumption.setInverterId(deviceForm.getInverterId());
        consumption.setPanelId(deviceForm.getPanelId());
        consumptionService.saveConsuption(consumption);
        log.info("Devices: Inverter: " + consumption.getInverterId().toString() + "  Panel: " + consumption.getPanelId().toString());
        return "redirect:/ajanlat/3?key=" + consumptionID;
    }

    @GetMapping("/ajanlat/3")
    public String getOfferStep3(@RequestParam(value="key") String consumptionID, Model model) {
        Consumption consumption = consumptionService.getConsumptionByconsumptionID(consumptionID);

        if (consumptionService.getConsumptionByconsumptionID(consumptionID) == null) {
            log.info("Step1 is not done, redirecting to /ajanlat/1.");
            return "redirect:/ajanlat/1";
        }

        DeviceForm deviceForm = new DeviceForm(consumption.getInverterId(), consumption.getPanelId());
        if (deviceForm.getInverterId() == null || deviceForm.getPanelId() == null) {
            log.info("Step2 is not done, redirecting to /ajanlat/2.");
            return "redirect:/ajanlat/2?key"+consumptionID;
        }
        EmailForm email = new EmailForm();

        consumption.setOfferId(1001 + consumptionService.rowCount());
        consumptionService.saveConsuption(consumption);
        Offer offer = offerService.createFromFormData(consumption, deviceForm);


        model.addAttribute("consumptionId", consumptionID);
        model.addAttribute("email", email);
        model.addAttribute(CONSUMPTION, consumption);
        model.addAttribute(STEP, '3');
        return "offer";
    }

    @PostMapping("/ajanlat/3")
    public String postOfferStep3(@RequestParam(value="key") String consumptionID, @ModelAttribute EmailForm email, Model model) {
        Consumption consumption = consumptionService.getConsumptionByconsumptionID(consumptionID);
        DeviceForm deviceForm = new DeviceForm(consumption.getInverterId(), consumption.getPanelId());
        Offer offer = offerService.createFromFormData(consumption, deviceForm);
        offer.setId(consumption.getOfferId());
        File ajanlatPdf = null;

        try {
            ajanlatPdf = pdfService.getPdf(offer);
            emailService.sendEmailWithPDf(email.getEmailAddress(), String.valueOf(offer.getId()), ajanlatPdf);
            model.addAttribute("success", true);
            model.addAttribute(STEP, "4");
            return "redirect:/ajanlat/4?key=" + consumptionID;

        } catch (UnirestException e) {

            log.warn("Pdf Server is unavailable. UnirestException is thrown.");
            e.printStackTrace();

        } catch (IOException e) {

            log.warn("Failed to convert PDF server's response to File.");
            e.printStackTrace();

        } catch (MessagingException e) {

            e.printStackTrace();
            model.addAttribute(PDF, ajanlatPdf.toURI());
            model.addAttribute("success", false);
        } catch (InvalidParameterException e) {

            log.warn("Failed to Send the email.");
            model.addAttribute(PDF, ajanlatPdf.toURI());
            model.addAttribute("success", false);
        }

            model.addAttribute(STEP, "4");
            return "offer";
        }

    @GetMapping("/ajanlat/4")
    public String getOfferStep4 (@RequestParam(value="key") String consumptionID, Model model){
        model.addAttribute(STEP, '4');
        return "offer";
    }


    @PostMapping("/ajanlat/network-upgrade")
    @ResponseBody
    public String isNetworkUpgradeNeededCheck (@RequestBody HashMap < String, String > payload){
        log.info("Request arrived to validate, payload: " + payload.toString());
        return String.valueOf(validationService.validateNetworkUpgrade(payload));
    }

    @PostMapping("/ajanlat/pdf")
    @ResponseBody
    public ResponseEntity<Resource> getPDF(@RequestParam(value="key") String consumptionID, @ModelAttribute ConsumptionForm consumptionForm) {

        Consumption consumption = consumptionService.getConsumptionByconsumptionID(consumptionID);
        DeviceForm device = new DeviceForm(consumption.getInverterId(), consumption.getPanelId());
        Offer offer = offerService.createFromFormData(consumption, device);
        offer.setId(consumption.getOfferId());
        offer.setCompany(CompanyEnum.TraditionalSolutions);
        File pdf = null;

        try {
            pdf = pdfService.getPdf(offer);
        } catch (UnirestException e) {
            log.warn("Pdf Server is unavailable. UnirestException is thrown.");
            e.printStackTrace();
        } catch (IOException e) {
            log.warn("Failed to convert PDF server's response to File.");
            e.printStackTrace();
        }
        Path path = null;
        if (pdf != null) {
            path = Paths.get(pdf.getAbsolutePath());
        }
        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok()
                .contentLength(pdf.length())
                .header("Content-Disposition", "attachment; filename=" + convertPdfName(pdf.getName()))
                .contentType(MediaType.parseMediaType("application/download"))
                .body(resource);

    }

    private String convertPdfName(String name){
        return name.substring(0, name.indexOf('@')) + ".pdf";
    }
}


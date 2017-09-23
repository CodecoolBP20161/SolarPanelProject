package com.codecool.controller;

import com.codecool.models.Inverter;
import com.codecool.models.LineItem;
import com.codecool.models.Offer;
import com.codecool.models.enums.CompanyEnum;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.models.forms.EmailForm;
import com.codecool.services.OfferService;
import com.codecool.services.PdfService;
import com.codecool.services.ValidationService;
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
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
public class OfferController {

    private EmailService emailService;
    private OfferService offerService;
    private PdfService pdfService;
    private ValidationService validationService;

    private final String CONSUMPTION = "consumption";
    private final String DEVICE = "deviceForm";
    private final String EMAIL = "email";
    private final String STEP = "step";
    private final String OFFER = "offer";
    private final String PDF = "pdf";
    private final String METRIC = "metric";

    @Autowired
    public OfferController(OfferService offerService, PdfService pdfService,
                           EmailService emailService, ValidationService validationService) {
        this.offerService = offerService;
        this.emailService = emailService;
        this.pdfService = pdfService;
        this.validationService = validationService;
    }

    @GetMapping("/ajanlat/1")
    public String getOfferStep1(Model model, HttpSession session) {
        ConsumptionForm consumptionForm = session.getAttribute(CONSUMPTION) == null ?
                new ConsumptionForm() : (ConsumptionForm) session.getAttribute(CONSUMPTION);

        session.setAttribute(DEVICE, null);
        session.setAttribute(OFFER, null);


        if (session.getAttribute(CONSUMPTION) != null){
            model.addAttribute(METRIC, consumptionForm.getMetric());
        } else {
                model.addAttribute(METRIC, "Ft");
        }
        model.addAttribute(CONSUMPTION, consumptionForm);
        model.addAttribute(STEP, '1');
        return "offer";
    }

    @PostMapping("/ajanlat/1")
    public String postOfferStep1(@ModelAttribute ConsumptionForm consumption, HttpSession session) {
        session.setAttribute(CONSUMPTION, consumption);
        log.info("Consumption: " + consumption.getValue() + consumption.getMetric() + " No.Phase: " + consumption.getPhase());
        return "redirect:/ajanlat/2";
    }

    @GetMapping("/ajanlat/2")
    public String getOfferStep2(Model model, HttpSession session) {
        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);
        if (consumption == null) {
            log.info("Step1 is not done, redirecting to /ajanlat/1.");
            return "redirect:/ajanlat/1";
        }
        DeviceForm pAndIForm = session.getAttribute(DEVICE) == null ?
                new DeviceForm() : (DeviceForm) session.getAttribute(DEVICE);

        double calculatedConsumption = offerService.calculateConsumption(consumption);
        List<Inverter> inverterList = offerService.calculateInverterList(calculatedConsumption);
        List<LineItem> solarPanelLineItems = offerService.getSolarPanelListAsLineItems(consumption);

        System.out.println(calculatedConsumption);
        if (calculatedConsumption > 12000){
            return "specialOfferNeeded";
        }

        model.addAttribute(DEVICE, pAndIForm);
        model.addAttribute("solarPanelLineItems", solarPanelLineItems);
        model.addAttribute("inverterList", inverterList);
        model.addAttribute(STEP, '2');
        return "offer";
    }

    @PostMapping("/ajanlat/2")
    public String postOfferStep2(@ModelAttribute DeviceForm device, HttpSession session) {
        session.setAttribute(DEVICE, device);
        log.info("Devices: InvID: " + device.getInverterId() + "  PanelId: " + device.getPanelId());
        return "redirect:/ajanlat/3";
    }

    @GetMapping("/ajanlat/3")
    public String getOfferStep3(Model model, HttpSession session) {
        DeviceForm deviceForm = (DeviceForm) session.getAttribute(DEVICE);
        if (deviceForm == null) {
            log.info("Step2 is not done, redirecting to /ajanlat/2.");
            return "redirect:/ajanlat/2";
        }
        EmailForm email = session.getAttribute(EMAIL) == null ?
                new EmailForm() : (EmailForm) session.getAttribute(EMAIL);

        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);
        consumption.setCompany(CompanyEnum.TraditionalSolutions);
        Offer offer = offerService.createFromFormData(consumption, deviceForm);
        session.setAttribute(OFFER, offer);

        model.addAttribute("email", email);
        model.addAttribute(CONSUMPTION, consumption);
        model.addAttribute(STEP, '3');
        return "offer";
    }

    @PostMapping("/ajanlat/3")
    public String postOfferStep3(@ModelAttribute EmailForm email, HttpSession session, Model model) {
        session.setAttribute(EMAIL, email);
        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);
        DeviceForm deviceForm = (DeviceForm) session.getAttribute(DEVICE);

        Offer offer = offerService.createFromFormData(consumption, deviceForm);
        session.setAttribute(OFFER, offer);
        File ajanlatPdf = null;

        try {
            ajanlatPdf = pdfService.getPdf(offer);
            emailService.sendEmailWithPDf(email.getEmailAddress(), String.valueOf(offer.getId()), ajanlatPdf);
            model.addAttribute("success", true);

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
    public String getOfferStep4 (Model model, HttpSession session){
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
    public ResponseEntity<Resource> getPDF(@ModelAttribute ConsumptionForm consumptionForm, HttpSession session) {

        Offer offer = (Offer) session.getAttribute(OFFER);

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
                .contentType(MediaType.parseMediaType("application/download"))
                .body(resource);
    }
}


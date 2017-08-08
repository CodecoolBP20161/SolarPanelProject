package com.codecool.controller;

import com.codecool.models.Inverter;
import com.codecool.models.LineItem;
import com.codecool.models.Offer;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.models.forms.EmailForm;
import com.codecool.services.OfferService;
import com.codecool.services.PdfService;
import com.codecool.services.email.EmailService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;

@Slf4j
@Controller
public class OfferController {


    private EmailService emailService;
    private OfferService offerService;
    private PdfService pdfService;

    private final String CONSUMPTION = "consumption";
    private final String DEVICE = "deviceForm";
    private final String EMAIL = "email";
    private final String STEP = "step";
    private final String OFFER = "offer";
    private final String PDF = "pdf";
    private final String METRIC = "metric";

    @Autowired
    public OfferController(OfferService offerService, PdfService pdfService, EmailService emailService) {
        this.offerService = offerService;
        this.emailService = emailService;
        this.pdfService = pdfService;
    }

    @GetMapping("/ajanlat/1")
    public String getOfferStep1(Model model, HttpSession session){
        ConsumptionForm consumptionForm = session.getAttribute(CONSUMPTION) == null ?
            new ConsumptionForm() : (ConsumptionForm) session.getAttribute(CONSUMPTION);


        if(session.getAttribute(CONSUMPTION) != null) model.addAttribute(METRIC, consumptionForm.getMetric());
        model.addAttribute(CONSUMPTION, consumptionForm);
        model.addAttribute(STEP, '1');
        return "offer";
    }
    @PostMapping("/ajanlat/1")
    public String postOfferStep1(@ModelAttribute ConsumptionForm consumption, HttpSession session){
        session.setAttribute(CONSUMPTION, consumption);
        log.info("Consumption: " + consumption.getValue() + consumption.getMetric() + " No.Phase: " + consumption.getPhase());
        return "redirect:/ajanlat/2";
    }

    @GetMapping("/ajanlat/2")
    public String getOfferStep2(Model model, HttpSession session){
        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);
        if(consumption == null){
            log.info("Step1 is not done, redirecting to /ajanlat/1.");
            return "redirect:/ajanlat/1";
        }
        DeviceForm pAndIForm = session.getAttribute(DEVICE) == null ?
                new DeviceForm() : (DeviceForm) session.getAttribute(DEVICE);

        int calculatedConsumption = offerService.calculateConsumption(consumption);
        List<Inverter> inverterList = offerService.calculateInverterList(calculatedConsumption, consumption.getPhase());
        List<LineItem> solarPanelLineItems = offerService.getSolarPanelListAsLineItems(consumption);

        model.addAttribute(DEVICE, pAndIForm);
        model.addAttribute("solarPanelLineItems",solarPanelLineItems);
        model.addAttribute("inverterList", inverterList);
        model.addAttribute(STEP, '2');
        return "offer";
    }

    @PostMapping("/ajanlat/2")
    public String postOfferStep2(@ModelAttribute DeviceForm device, HttpSession session){
        session.setAttribute(DEVICE, device);
        log.info("Devices: InvID: " + device.getInverterId() + "  PanelId: " + device.getPanelId());
        return "redirect:/ajanlat/3";
    }


    @GetMapping("/ajanlat/3")
    public String getOfferStep3(Model model, HttpSession session){
        DeviceForm deviceForm = (DeviceForm) session.getAttribute(DEVICE);

        if (!deviceForm.isValid()) {
            log.info("Step2 is not done, redirecting to ajanlat/2, InvID: " + deviceForm.getInverterId() + " PanelID: " + deviceForm.getPanelId());
            return "redirect:/ajanlat/2";
        }

        EmailForm email = session.getAttribute(EMAIL) == null ?
                new EmailForm() : (EmailForm) session.getAttribute(EMAIL);

        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);


        List<LineItem> offerItem =  offerService.getOffer(consumption,
                                                          Integer.parseInt(deviceForm.getPanelId()),
                                                          Integer.parseInt(deviceForm.getInverterId()));

        for (LineItem lineItem : offerItem) {
            System.out.println(lineItem.getName()+ " " + lineItem.getPrice() + " " + lineItem.getQuantity());
        }

        model.addAttribute("email", email);
        model.addAttribute(STEP, '3');
        return "offer";
    }

    @PostMapping("/ajanlat/3")
    public String postOfferStep3(@ModelAttribute EmailForm email, HttpSession session, Model model){
        session.setAttribute(EMAIL, email);

        Offer offer = (Offer) session.getAttribute("offer");
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
        if(pdf != null) {
            try {
                emailService.sendEmailWithPDf(email.getEmailAddress(), "", pdf);
                model.addAttribute(PDF, pdf.toURI());
                model.addAttribute("success", true);
            } catch (MessagingException e){
                e.printStackTrace();
                model.addAttribute("success", false);
            } catch (InvalidParameterException e) {
                log.warn("Failed to Send the email.");
                model.addAttribute("success", false);
            }
        }
        model.addAttribute(STEP, "4");
        return "offer";
    }

    @GetMapping("/ajanlat/4")
    public String getOfferStep4(Model model, HttpSession session){
        model.addAttribute(STEP, '4');
        return "offer";
    }
}
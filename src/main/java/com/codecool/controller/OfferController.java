package com.codecool.controller;

import com.codecool.models.Inverter;
import com.codecool.models.Offer;
import com.codecool.models.SolarPanel;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.models.forms.EmailForm;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;
import com.codecool.services.OfferService;
import com.codecool.services.PdfService;
import com.codecool.models.PdfServerException;
import com.codecool.services.email.EmailService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class OfferController {

    private SolarPanelRepository solarPanelRepository;
    private InverterRepository inverterRepository;
    private EmailService emailService;
    private OfferService offerService;
    private PdfService pdfService;

    private final String CONSUMPTION = "consumption";
    private final String DEVICE = "deviceForm";
    private final String EMAIL = "email";
    private final String STEP = "step";
    private final String OFFER = "offer";
    private final String PDF = "pdf";
    private final String CONSUMPTIONTYPE = "consumptionType";

    @Autowired
    public OfferController(SolarPanelRepository solarPanelRepository, InverterRepository inverterRepository,
                           OfferService offerService, EmailService emailService, PdfService pdfService) {
        this.solarPanelRepository = solarPanelRepository;
        this.inverterRepository = inverterRepository;
        this.offerService = offerService;
        this.emailService = emailService;
        this.pdfService = pdfService;
    }

    @GetMapping("/ajanlat/1")
    public String getOfferStep1(Model model, HttpSession session){
        ConsumptionForm consumptionForm = session.getAttribute(CONSUMPTION) == null ?
            new ConsumptionForm() : (ConsumptionForm) session.getAttribute(CONSUMPTION);


        model.addAttribute(CONSUMPTION, consumptionForm);
        if(session.getAttribute(CONSUMPTION) != null) model.addAttribute()
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
        if(session.getAttribute(CONSUMPTION) == null){
            log.info("Step1 is not done, redirecting to ajanlat 1.");
            return "redirect:/ajanlat/1";
        }

        //findAllByOrderByCapacity

        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);

        int calculatedConsumption = offerService.calculateConsumption(consumption.getValue(), consumption.getMetric());

        List<Inverter> inverterList = offerService.calculateInverterList(calculatedConsumption, consumption.getPhase());

        List<SolarPanel> solarPanelList = offerService.getSolarPanelList();

        DeviceForm pAndIForm = session.getAttribute(DEVICE) == null ?
                new DeviceForm() : (DeviceForm) session.getAttribute(DEVICE);

        model.addAttribute(DEVICE, pAndIForm);
        model.addAttribute("solarPanelList", solarPanelList);
        model.addAttribute("inverterList", inverterList);
        model.addAttribute(STEP, '2');
        return "offer";
    }

    @PostMapping("/ajanlat/2")
    public String postOfferStep2(@ModelAttribute DeviceForm device, HttpSession session){
        session.setAttribute(DEVICE, device);
        log.info("Devices: " + device.getInverterId() + " " + device.getPanelId());
        return "redirect:/ajanlat/3";
    }


    @GetMapping("/ajanlat/3")
    public String getOfferStep3(Model model, HttpSession session){
        DeviceForm deviceForm = (DeviceForm) session.getAttribute(DEVICE);

        EmailForm email = session.getAttribute(EMAIL) == null ?
                new EmailForm() : (EmailForm) session.getAttribute(EMAIL);

        if (session.getAttribute(DEVICE) == null || !deviceForm.isvalid()) {
            log.info("Step2 is not done, redirecting to ajanlat/2, InvID: " + deviceForm.getInverterId() + " PanelID: " + deviceForm.getPanelId());
            return "redirect:/ajanlat/2";
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
            e.printStackTrace();
            log.warn("Pdf Server is unavailable. UnirestException is thrown.");
        } catch (IOException e) {
            log.warn("Failed to convert PDF server's response to File.");
            e.printStackTrace();
        }
        if(pdf != null) {
            try {
                emailService.sendEmailWithPDf(email.getEmailAddress(), String.valueOf(offer.getId()), pdf);
                model.addAttribute("success", true);
            } catch (MessagingException e) {
                log.warn("Failed to Send the emails.");
                e.printStackTrace();
                model.addAttribute("success", false);
            }
        }
        model.addAttribute(PDF, "pdf");
        model.addAttribute(STEP, "4");
        return "offer";
    }

    @GetMapping("/ajanlat/4")
    public String getOfferStep4(Model model, HttpSession session){
        model.addAttribute(STEP, '4');
        return "offer";
    }

    @PostMapping("/ajanlat/4")
    public String postOfferStep4(Model model, HttpSession session){



        model.addAttribute(STEP, '4');
        return "offer";
    }

}
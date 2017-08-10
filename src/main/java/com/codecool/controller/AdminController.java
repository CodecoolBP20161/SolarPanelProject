package com.codecool.controller;
    import com.codecool.models.Inverter;
    import com.codecool.models.LineItem;
    import com.codecool.models.Offer;
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
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import javax.mail.MessagingException;
    import javax.servlet.http.HttpSession;
    import java.io.File;
    import java.io.IOException;
    import java.security.InvalidParameterException;
    import java.util.HashMap;
    import java.util.List;

@Slf4j
@Controller
public class AdminController {
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
    public AdminController(OfferService offerService, PdfService pdfService,
                           EmailService emailService, ValidationService validationService) {
        this.offerService = offerService;
        this.emailService = emailService;
        this.pdfService = pdfService;
        this.validationService = validationService;
    }

    @GetMapping("/admin/fogyasztas")
    public String getConsumptionData(Model model, HttpSession session){
        ConsumptionForm consumptionForm = session.getAttribute(CONSUMPTION) == null ?
                new ConsumptionForm() : (ConsumptionForm) session.getAttribute(CONSUMPTION);

        if(session.getAttribute(CONSUMPTION) != null) model.addAttribute(METRIC, consumptionForm.getMetric());
        model.addAttribute(CONSUMPTION, consumptionForm);
        model.addAttribute(STEP, "admin1");
        return "offer";
    }

    @PostMapping("/admin/fogyasztas")
    public String postConsumptionData(@ModelAttribute ConsumptionForm consumption, HttpSession session){
        session.setAttribute(CONSUMPTION, consumption);
        log.info("Consumption: " + consumption.getValue() + consumption.getMetric() + " No.Phase: " + consumption.getPhase());
        return "redirect:/admin/fogyasztas";
    }

    @GetMapping("/admin/eszkozok")
    public String getOfferStep2(Model model, HttpSession session){
        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);
        if(consumption == null){
            log.info("Step1 is not done, redirecting to fogyasztas.");
            return "redirect:/admin/fogyasztas";
        }
        DeviceForm pAndIForm = session.getAttribute(DEVICE) == null ?
                new DeviceForm() : (DeviceForm) session.getAttribute(DEVICE);

        int calculatedConsumption = offerService.calculateConsumption(consumption);
        List<Inverter> inverterList = offerService.calculateInverterList(calculatedConsumption, consumption.getPhase());
        List<LineItem> solarPanelLineItems = offerService.getSolarPanelListAsLineItems(consumption);

        model.addAttribute(DEVICE, pAndIForm);
        model.addAttribute("solarPanelLineItems",solarPanelLineItems);
        model.addAttribute("inverterList", inverterList);
        model.addAttribute(STEP, "admin2");
        return "offer";
    }

    @PostMapping("/admin/eszkozok")
    public String postOfferStep2(@ModelAttribute DeviceForm device, HttpSession session){
        session.setAttribute(DEVICE, device);
        log.info("Devices: InvID: " + device.getInverterId() + "  PanelId: " + device.getPanelId());
        return "redirect:/ajanlat/3";
    }

    @GetMapping("admin/szerkeszto")
    public String getOfferStep3(Model model, HttpSession session){
        DeviceForm deviceForm = (DeviceForm) session.getAttribute(DEVICE);

        if(deviceForm == null || !deviceForm.isValid()){
            log.info("Step2 is not done, redirecting to /admin/eszkozok.");
            return "redirect:/admin/eszkozok";
        }
        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);
        Offer offer = new Offer();

        offer.setCompany(consumption.getCompany());
        List<LineItem> offerItem =  offerService.getLineItems(consumption, deviceForm);

        offerItem.forEach(offer::addLineItem);
        model.addAttribute(OFFER, offer);
        model.addAttribute(STEP, "admin3");
        return "offer";
    }




}

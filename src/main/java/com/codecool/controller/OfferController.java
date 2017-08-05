package com.codecool.controller;

import com.codecool.models.Inverter;
import com.codecool.models.LineItem;
import com.codecool.models.SolarPanel;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.models.forms.EmailForm;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;
import com.codecool.services.OfferService;
import com.codecool.services.SolarPanelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
//@SessionAttributes({"offer"}) "consumption" and "deviceForm" "email" are removed
public class OfferController {

    private SolarPanelRepository solarPanelRepository;
    private InverterRepository inverterRepository;
    private OfferService offerService;
    private SolarPanelService solarPanelService;
    private final String CONSUMPTION = "consumption";
    private final String DEVICE = "deviceForm";
    private final String EMAIL = "email";
    private final String STEP = "step";
    private final String OFFER = "offer";

    @Autowired
    public OfferController(SolarPanelRepository solarPanelRepository, InverterRepository inverterRepository, OfferService offerService, SolarPanelService solarPanelService) {
        this.solarPanelRepository = solarPanelRepository;
        this.inverterRepository = inverterRepository;
        this.offerService = offerService;
        this.solarPanelService = solarPanelService;
    }

    @GetMapping("/ajanlat/1")
    public String getOfferStep1(Model model, HttpSession session){
        ConsumptionForm consumptionForm = session.getAttribute(CONSUMPTION) == null ?
            new ConsumptionForm() : (ConsumptionForm) session.getAttribute(CONSUMPTION);
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
        if(session.getAttribute(CONSUMPTION) == null){
            log.info("Step1 is not done, redirecting to ajanlat 1.");
            return "redirect:/ajanlat/1";
        }

        //findAllByOrderByCapacity

        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);

        int calculatedConsumption = offerService.calculateConsumption(consumption.getValue(), consumption.getMetric());

        List<Inverter> inverterList = offerService.calculateInverterList(calculatedConsumption, consumption.getPhase());

        List<SolarPanel> solarPanelList = offerService.getSolarPanelList();

        List<LineItem> solarPanelLineItems = new ArrayList<>();

        DeviceForm pAndIForm = session.getAttribute(DEVICE) == null ?
                new DeviceForm() : (DeviceForm) session.getAttribute(DEVICE);

        LineItem solarPanelItem;

        for (SolarPanel solarPanel : solarPanelList) {
            solarPanelItem = new LineItem(solarPanel);
            solarPanelItem.setQuantity(solarPanelService.callculateSolarPanelPiece(consumption.getValue(), consumption.getMetric(), solarPanel.getCapacity()));
            solarPanelLineItems.add(solarPanelItem);
        }

        model.addAttribute(DEVICE, pAndIForm);
        model.addAttribute("solarPanelList", solarPanelList);
        model.addAttribute("solarPanelLineItems",solarPanelLineItems);
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

        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);


        if (session.getAttribute(DEVICE) == null || deviceForm.getInverterId() == null
                || deviceForm.getPanelId() == null) {
            log.info("Step2 is not done, redirecting to ajanlat/2" + deviceForm.getInverterId() + deviceForm.getPanelId());
            return "redirect:/ajanlat/2";
        }


        List<LineItem> offerItem =  offerService.getOffer(consumption.getValue(), consumption.getMetric(), consumption.getPhase(),
                Integer.parseInt(deviceForm.getPanelId()), Integer.parseInt(deviceForm.getInverterId()));

        for (LineItem offer1 : offerItem) {
            System.out.println(offer1.getName()+ " " + offer1.getPrice() + " " + offer1.getQuantity());
        }

        model.addAttribute("email", email);
        model.addAttribute(STEP, '3');
        return "offer";
    }

    @PostMapping("/ajanlat/3")
    public String getOfferStep3(@ModelAttribute EmailForm email, HttpSession session){
        session.setAttribute(EMAIL, email);
        log.info("Entered email: " + email.getEmailAddress());
        return "redirect:/ajanlat/4";
    }

    @GetMapping("/ajanlat/4")
    public String getOfferStep4(Model model, HttpSession session){
        model.addAttribute(STEP, '4');
        return "offer";
    }

}
package com.codecool.controller;

import com.codecool.models.SolarPanel;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.PanelAndInverterForm;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
public class MainController {

    @Autowired
    private SolarPanelRepository solarPanelRepository;


    @GetMapping("/")
    public String getIndex(){
        return "index";
    }

    @GetMapping("/rolunk")
    public String getAbout(){
        return "aboutus";
    }

    @GetMapping("/ajanlat/1")
    public String getOfferStep1(Model model){
        model.addAttribute("consumption", new ConsumptionForm());
        model.addAttribute("step", '1');
        return "offer";
    }
    @PostMapping("/ajanlat/1")
    public String postOfferStep1(Model model, @ModelAttribute ConsumptionForm consumption){
        log.info("Consumption: " + consumption.getValue() + " " + consumption.getMetric());
        return "redirect:/ajanlat/2";
    }

    @GetMapping("/ajanlat/2")
    public String getOfferStep2(Model model){
        List<SolarPanel> solarPanelList = solarPanelRepository.findAll();
        model.addAttribute("deviceForm", new PanelAndInverterForm());
        model.addAttribute("solarPanelList", solarPanelList);
        model.addAttribute("step", '2');
        return "offer";
    }

    @PostMapping("/ajanlat/2")
    public String postOfferStep2(Model model, @ModelAttribute PanelAndInverterForm device){
        log.info("Devices: " + device.getInverterId() + " " + device.getPanelId());
        model.addAttribute("step", '3');
        return "offer";
    }


    @GetMapping("/ajanlat/3")
    public String getOfferStep3(Model model){
        model.addAttribute("step", '3');
        return "offer";
    }

    @GetMapping("/ajanlat/4")
    public String getOfferStep4(Model model){
        model.addAttribute("step", '4');
        return "offer";
    }

}
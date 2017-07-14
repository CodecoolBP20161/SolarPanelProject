package com.codecool.controller;

import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.PanelAndInverterForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class MainController {

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
        ConsumptionForm consumptionForm = new ConsumptionForm();
        consumptionForm.setPhase(1);
        model.addAttribute("consumption", consumptionForm);
        model.addAttribute("step", '1');
        return "offer";
    }
    @PostMapping("/ajanlat/1")
    public String postOfferStep1(Model model, @ModelAttribute ConsumptionForm consumption){
        log.info("Consumption: " + consumption.getValue() + " " + consumption.getMetric() + "\n Number of phases: " + consumption.getPhase());
        return "redirect:/ajanlat/2";
    }

    @GetMapping("/ajanlat/2")
    public String getOfferStep2(Model model){
        model.addAttribute("deviceForm", new PanelAndInverterForm());
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
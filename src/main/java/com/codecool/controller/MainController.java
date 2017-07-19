package com.codecool.controller;

import com.codecool.models.Inverter;
import com.codecool.models.SolarPanel;
import com.codecool.models.forms.ConsumptionForm;
import com.codecool.models.forms.DeviceForm;
import com.codecool.models.forms.EmailForm;
import com.codecool.repositories.InverterRepository;
import com.codecool.repositories.SolarPanelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@SessionAttributes({ "email", "offer"}) //"consumption" and "deviceForm" are removed
public class MainController {

    private SolarPanelRepository solarPanelRepository;
    private InverterRepository inverterRepository;
    private final String CONSUMPTION = "consumption";
    private final String DEVICE = "deviceForm";
    private final String EMAIL = "email";
    private final String STEP = "step";
    private final String OFFER = "offer";

    @Autowired
    public MainController(InverterRepository inverterRepository, SolarPanelRepository solarPanelRepository){
        this.solarPanelRepository = solarPanelRepository;
        this.inverterRepository = inverterRepository;
    }

    @GetMapping("/")
    public String getIndex(){
        return "index";
    }

    @GetMapping("/rolunk")
    public String getAbout(){
        return "aboutus";
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

        ConsumptionForm consumption = (ConsumptionForm) session.getAttribute(CONSUMPTION);
        List<Inverter> inverterList = (consumption.getValue() >= 11 &
                inverterRepository.findByCustomerValue(((int) consumption.getValue()) *1000).size() == 0) ?
                inverterRepository.findAllOvertTenThousand():
                inverterRepository.findByCustomerValue(((int) consumption.getValue()) *1000);

        List<SolarPanel> solarPanelList = solarPanelRepository.findAllByOrderByCapacityAscPriceAsc();

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
        if (session.getAttribute(DEVICE) == null) {
            return "redirect:/ajanlat/2";
        }
        else {
            DeviceForm deviceForm = (DeviceForm) session.getAttribute(DEVICE);
            if (deviceForm.getInverterId() == null || deviceForm.getPanelId() == null) {
                log.info("Step2 is not done, redirecting to ajanlat 2." + deviceForm.getInverterId() + deviceForm.getPanelId());
                return "redirect:/ajanlat/2";
            }

        }

        EmailForm email = session.getAttribute(EMAIL) == null ?
                new EmailForm() : (EmailForm) session.getAttribute(EMAIL);

        model.addAttribute("email", email);
        model.addAttribute(STEP, '3');
        return "offer";
    }
    @PostMapping("/ajanlat/3")
    public String getOfferStep3(Model model, @ModelAttribute EmailForm email, HttpSession session){
        session.setAttribute(EMAIL, email);
        log.info("Entered email: ", email.getEmail());
        return "offer";
    }

    @GetMapping("/ajanlat/4")
    public String getOfferStep4(Model model){
        model.addAttribute(STEP, '4');
        return "offer";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

}
package com.codecool.controller;

import com.codecool.models.ReadyProduct;
import com.codecool.services.DataLoader;
import com.codecool.services.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Slf4j
@Controller
public class MainController {

    @Autowired
    EmailService emailService;

    @Autowired
    DataLoader dataLoader;


    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/rolunk")
    public String getAbout() {
        return "aboutus";
    }

    @GetMapping("/finanszirozas")
    public String getFinancing() {
        return "financing";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @GetMapping("/aszf")
    public String getAszf() {
        return "aszf";
    }

    @GetMapping("/referencia")
    public String getPhotos() {
        return "galery";
    }

    @GetMapping("/termekek/{brand}")
    public String getReadyProduct(@PathVariable String brand ,Model model) {
        List<ReadyProduct> offers = dataLoader.loadPresetProduct(brand);
        model.addAttribute("allProduct", offers);
        model.addAttribute("productBrand", brand);
        return "readyProduct";
    }
}


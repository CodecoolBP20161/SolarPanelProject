package com.codecool.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/ajanlat")
    public String getOffer(){
        return "offer";
    }

}
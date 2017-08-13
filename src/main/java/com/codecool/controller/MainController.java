package com.codecool.controller;

import com.codecool.models.forms.EmailForm;
import com.codecool.services.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.InvalidParameterException;

@Slf4j
@Controller
public class MainController {

    @Autowired
    EmailService emailService;


    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "index";
    }

    @PostMapping("/")
    public String postSendMessageFromIndex(@ModelAttribute EmailForm emailForm, Model model) {
        try {
            emailService.sendQuestionEmail(emailForm);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            log.warn("Failed to Send the email.");
        }
        model.addAttribute("emailForm", new EmailForm());
        return "index";
    }

    @GetMapping("/rolunk")
    public String getAbout() {
        return "aboutus";
    }


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @GetMapping("/üzenet")
    public String getSendMessage(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "contact";
    }

    @PostMapping("/üzenet")
    public String postSendMessage(@ModelAttribute EmailForm emailForm, Model model) {
        try {
            emailService.sendQuestionEmail(emailForm);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            log.warn("Failed to Send the email.");
        }
        model.addAttribute("emailForm", new EmailForm());
        return "index";
    }
}


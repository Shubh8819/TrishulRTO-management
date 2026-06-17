package com.trishul.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.RestController;

@Controller
public class ModuleController {

    @GetMapping("/")
    public String getModule(Model model){
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("pageSubtitle", "Welcome back, Admin! Here's what's happening today.");
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("licenceCount", 12);
        model.addAttribute("totalLicences", 150);
        //model.addAttribute("pageContent", "licence/dashboard :: pageContent");
        model.addAttribute("pageContent", "licence/dashboard");
        return "fragments/layout";
    }
}

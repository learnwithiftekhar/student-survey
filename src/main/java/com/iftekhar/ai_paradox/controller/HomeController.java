package com.iftekhar.ai_paradox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @GetMapping
    public String showForm() {
        return "studentForm";
    }
}

package com.example.employeemanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/marketing")
public class MarketingController {

    @GetMapping
    public String showMarketingPage() {
        return "marketing";
    }
}
package com.example.employeemanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/it")
public class ITController {

    @GetMapping
    public String showITPage() {
        return "it";
    }
}

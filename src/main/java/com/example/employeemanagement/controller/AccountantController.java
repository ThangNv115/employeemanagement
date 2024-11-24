package com.example.employeemanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accountant")
public class AccountantController {

    @GetMapping
    public String showAccountantPage() {
        return "accountant";
    }
}

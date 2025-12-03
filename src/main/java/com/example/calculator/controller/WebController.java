package com.example.calculator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "calculator";
    }

    @GetMapping("/calculator")
    public String calculator() {
        return "calculator";
    }
}

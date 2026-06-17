package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PredictionViewController {
    
    @GetMapping("/predict")
    public String predictionForm() {
        return "predict";
    }
}

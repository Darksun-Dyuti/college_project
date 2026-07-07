package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PredictionViewController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/predict")
    public String predictionForm(Model model) {
        User user = getCurrentUser();
        if (user != null) {
            int hospitalsCount = recommendationService.getRecommendedHospitals(user).size();
            int doctorsCount = recommendationService.getRecommendedDoctors(user).size();
            model.addAttribute("hasConditions", (hospitalsCount > 0 || doctorsCount > 0));
            model.addAttribute("hospitalsCount", hospitalsCount);
            model.addAttribute("doctorsCount", doctorsCount);
        } else {
            model.addAttribute("hasConditions", false);
            model.addAttribute("hospitalsCount", 0);
            model.addAttribute("doctorsCount", 0);
        }
        return "predict";
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return userRepository.findByUsername(auth.getName()).orElse(null);
        }
        return null;
    }
}

package com.example.demo.controller;

import com.example.demo.model.InsuranceRequest;
import com.example.demo.model.InsuranceResponse;
import com.example.demo.service.MLPredictionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import java.util.Map;
import com.example.demo.service.UserService;
import com.example.demo.entity.pastPredictions;
import com.example.demo.repository.PastPredictionsRepository;
import com.example.demo.repository.HealthProfileRepository;
import com.example.demo.entity.HealthProfile;
import java.util.Locale;

@RestController
@RequestMapping("/api")
public class PredictController {

    private final MLPredictionService mlPredictionService;
    private final UserService userService;
    private final PastPredictionsRepository pastPredictionsRepository;
    private final HealthProfileRepository healthProfileRepository;

    public PredictController(MLPredictionService mlPredictionService, UserService userService, PastPredictionsRepository pastPredictionsRepository, HealthProfileRepository healthProfileRepository) {
        this.mlPredictionService = mlPredictionService;
        this.userService = userService;
        this.pastPredictionsRepository = pastPredictionsRepository;
        this.healthProfileRepository = healthProfileRepository;
    }

    @PostMapping(value = "/predict", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public InsuranceResponse predict(@RequestBody InsuranceRequest req,Authentication authentication) {
        if (req.getAge() == null || req.getAge() <= 0 || req.getAge() > 120) {
            return new InsuranceResponse("Error: Age must be between 1 and 120", null);
        }
        if (req.getBmi() == null || req.getBmi() <= 0 || req.getBmi() > 80) {
            return new InsuranceResponse("Error: BMI must be between 1 and 80", null);
        }
        if (req.getGender() == null || req.getGender().trim().isEmpty()) {
            return new InsuranceResponse("Error: Gender is required", null);
        }
        if ((req.getLocation() == null || req.getLocation().trim().isEmpty()) && (req.getState() == null || req.getState().trim().isEmpty())) {
            return new InsuranceResponse("Error: State is required", null);
        }
        if (req.getKids() == null || req.getKids() < 0 || req.getKids() > 10) {
            return new InsuranceResponse("Error: Number of kids must be between 0 and 10", null);
        }
    
        String username=authentication.getName();
        Long userId=userService.findByUsername(username).get().getId();

        // Validate new fields with defaults
        String income = resolveIncomeBucket(req);
        String employment = (req.getEmployment() != null && !req.getEmployment().trim().isEmpty()) ? req.getEmployment() : "employed";
        Integer healthScore = resolveHealthScoreFromProfile(userId, req.getHealthScore());
        Integer exerciseFrequency = (req.getExerciseFrequency() != null) ? req.getExerciseFrequency() : 3;
        String education = (req.getEducation() != null && !req.getEducation().trim().isEmpty()) ? req.getEducation() : "bachelor";
        String maritalStatus = (req.getMaritalStatus() != null && !req.getMaritalStatus().trim().isEmpty()) ? req.getMaritalStatus() : "single";
        Integer yearsInsured = (req.getYearsInsured() != null) ? req.getYearsInsured() : 0;
        String region = resolveRegion(req.getState(), req.getLocation());

        // Validate ranges
        if (healthScore < 1 || healthScore > 10) {
            return new InsuranceResponse("Error: Health score must be between 1 and 10", null);
        }
        if (exerciseFrequency < 0 || exerciseFrequency > 7) {
            return new InsuranceResponse("Error: Exercise frequency must be between 0 and 7 days per week", null);
        }
        if (yearsInsured < 0 || yearsInsured > 50) {
            return new InsuranceResponse("Error: Years insured must be between 0 and 50", null);
        }

        // Validate enum-like values
        String[] validIncomes = {"low", "medium", "high", "very_high"};
        if (!java.util.Arrays.asList(validIncomes).contains(income.toLowerCase())) {
            return new InsuranceResponse("Error: Income must be one of: low, medium, high, very_high", null); 
        }

        String[] validEmployments = {"employed", "self_employed", "unemployed", "retired"};
        if (!java.util.Arrays.asList(validEmployments).contains(employment.toLowerCase())) {
            return new InsuranceResponse("Error: Employment must be one of: employed, self_employed, unemployed, retired", null);
        }

        String[] validEducations = {"high_school", "bachelor", "master", "phd"};
        if (!java.util.Arrays.asList(validEducations).contains(education.toLowerCase())) {
            return new InsuranceResponse("Error: Education must be one of: high_school, bachelor, master, phd", null);
        }

        String[] validMarital = {"single", "married", "divorced", "widowed"};
        if (!java.util.Arrays.asList(validMarital).contains(maritalStatus.toLowerCase())) {
            return new InsuranceResponse("Error: Marital status must be one of: single, married, divorced, widowed", null);
        }

        String[] validLocations = {"northeast", "southeast", "southwest", "northwest"};
        if (!java.util.Arrays.asList(validLocations).contains(region.toLowerCase())) {
            return new InsuranceResponse("Error: Could not map selected state/location to a valid region", null);
        }

        // Leave model null so Python selects the best available trained model automatically.
        String selectedModel = null;
        
        Map<String, Object> predictionResult = mlPredictionService.predictInsuranceCost(
                req.getAge(),
                req.getGender(),
                req.getBmi(),
                req.getKids(),
                req.getSmoker() != null ? req.getSmoker() : false,
                region,
                income,
                employment,
                healthScore,
                exerciseFrequency,
                education,
                maritalStatus,
                yearsInsured,
                selectedModel,
                userId
        );
        
        String usedModel = (String) predictionResult.get("model");
        double predictedCost = ((Number) predictionResult.get("prediction")).doubleValue();

        // Generate a friendly response with suggestions
        StringBuilder response = new StringBuilder();
        StringBuilder tempResponse=new StringBuilder();
        pastPredictions pastpredictions=new pastPredictions();
        // response.append("=== Insurance Cost Estimate ===\n");
        
        // tempResponse.append("=== Insurance Cost Estimate ===\n");
        
        // response.append(String.format("Model Used: %s\n", usedModel.replace("_", " ").toUpperCase()));
        // response.append(String.format("Estimated Annual Cost: $%.2f\n\n", predictedCost));
        
        // tempResponse.append("===Total Estimated Annual Cost (INR)===\n");
        // tempResponse.append(String.format("Estimated Annual Cost: INR %.2f\n\n", predictedCost));

        // response.append("=== Your Profile ===\n");
        StringBuilder profileBuilder=new StringBuilder();
        profileBuilder.append(String.format("Age: %d | BMI: %.1f | Gender: %s\n", req.getAge(), req.getBmi(), req.getGender()));
        profileBuilder.append(String.format("Smoker: %s | Children: %d | State: %s | Region: %s\n",
            (req.getSmoker() != null && req.getSmoker()) ? "Yes" : "No", req.getKids(),
            req.getState() != null ? req.getState() : "NA", region));
        profileBuilder.append(String.format("Income: %s | Employment: %s\n", income, employment));
        profileBuilder.append(String.format("Health Score: %d/10 | Exercise: %d days/week | Education: %s\n", 
            healthScore, exerciseFrequency, education));
        profileBuilder.append(String.format("Marital Status: %s | Years Insured: %d\n\n", maritalStatus, yearsInsured));
        pastpredictions.setProfile(profileBuilder.toString());
        pastpredictions.setModelUsed(usedModel);
        // response.append("=== Personalized Suggestions to Reduce Cost ===\n");
        
        // Generate suggestions based on input

        StringBuilder suggestions = new StringBuilder();
        if (req.getSmoker() != null && req.getSmoker()) {
            suggestions.append("1. Consider quitting smoking - this could reduce your premium by up to 50%\n").append(System.lineSeparator());
        } else {
            suggestions.append("1. Maintain your non-smoker status - this keeps your premiums lower\n").append(System.lineSeparator());
        }

        if (req.getBmi() != null && req.getBmi() > 30) {
            suggestions.append("2. Work on reducing your BMI through diet and exercise - lower BMI means lower costs\n").append(System.lineSeparator());
        } else if (req.getBmi() != null && req.getBmi() > 25) {
            suggestions.append("2. Maintain a healthy BMI to keep insurance costs stable\n").append(System.lineSeparator());
        } else {
            suggestions.append("2. Keep maintaining your healthy BMI - this is excellent for your health and insurance costs\n").append(System.lineSeparator());
        }

        if (healthScore < 6) {
            suggestions.append("3. Improve your health score by regular check-ups and preventive care\n").append(System.lineSeparator());
        } else if (healthScore >= 8) {
            suggestions.append("3. Excellent health score! Keep up with your wellness routine\n").append(System.lineSeparator());
        }

        if (exerciseFrequency < 3) {
            suggestions.append("4. Increase exercise frequency - aim for at least 3-5 days per week for better health\n").append(System.lineSeparator());
        } else {
            suggestions.append("4. Excellent exercise routine! Keep maintaining your fitness level\n").append(System.lineSeparator());
        }

        if (education.equalsIgnoreCase("high_school")) {
            suggestions.append("5. Consider pursuing higher education for better career and health insurance prospects\n").append(System.lineSeparator());
        }

        suggestions.append("6. Schedule regular health check-ups to prevent chronic conditions\n").append(System.lineSeparator());

        pastpredictions.setEstimatedCost(predictedCost);
        pastpredictions.setAdvice(suggestions.toString());
        pastpredictions.setUserId(userId);

        pastPredictionsRepository.save(pastpredictions);
        if (req.getExistingCondition() != null && !req.getExistingCondition().trim().isEmpty()) {
            response.append("\nNote: Your existing condition(s) may impact the final premium. Please consult with an insurance agent for detailed information.\n");
        }

        return new InsuranceResponse(predictedCost);
    }

    private String resolveIncomeBucket(InsuranceRequest req) {
        if (req.getAnnualIncome() != null && req.getAnnualIncome() > 0) {
            double annualIncome = req.getAnnualIncome();
            if (annualIncome < 300000) {
                return "low";
            }
            if (annualIncome < 1000000) {
                return "medium";
            }
            if (annualIncome < 2500000) {
                return "high";
            }
            return "very_high";
        }
        if (req.getIncome() == null || req.getIncome().trim().isEmpty()) {
            return "medium";
        }
        return req.getIncome().trim().toLowerCase(Locale.ROOT);
    }

    private Integer resolveHealthScoreFromProfile(Long userId, Integer requestHealthScore) {
        HealthProfile profile = healthProfileRepository.findByUserId(userId).orElse(null);
        if (profile == null) {
            return requestHealthScore != null ? requestHealthScore : 7;
        }

        int score = 10;
        if (profile.isHasDiabetes()) score -= 1;
        if (profile.isHasHypertension()) score -= 1;
        if (profile.isHasAsthma()) score -= 1;
        if (profile.isHasArthritis()) score -= 1;
        if (profile.isHasHeartDisease()) score -= 2;
        if (profile.isHasKidneyDisease()) score -= 2;
        if (profile.isHasCancer()) score -= 2;
        if (profile.getOtherConditions() != null && !profile.getOtherConditions().trim().isEmpty()) score -= 1;

        return Math.max(1, Math.min(10, score));
    }

    private String resolveRegion(String state, String location) {
        if (state != null && !state.trim().isEmpty()) {
            String normalized = state.trim().toLowerCase(Locale.ROOT);

            if (normalized.matches("delhi|haryana|punjab|himachal pradesh|uttarakhand|uttar pradesh|jammu and kashmir|ladakh|chandigarh|rajasthan")) {
                return "northwest";
            }
            if (normalized.matches("bihar|jharkhand|west bengal|odisha|sikkim|assam|arunachal pradesh|meghalaya|tripura|mizoram|manipur|nagaland")) {
                return "northeast";
            }
            if (normalized.matches("maharashtra|goa|gujarat|madhya pradesh|chhattisgarh|dadra and nagar haveli and daman and diu")) {
                return "southwest";
            }
            if (normalized.matches("karnataka|kerala|tamil nadu|andhra pradesh|telangana|puducherry|andaman and nicobar islands|lakshadweep")) {
                return "southeast";
            }
        }

        if (location != null && !location.trim().isEmpty()) {
            String normalizedLocation = location.trim().toLowerCase(Locale.ROOT);
            if (normalizedLocation.equals("north")) return "northwest";
            if (normalizedLocation.equals("east")) return "northeast";
            if (normalizedLocation.equals("west")) return "southwest";
            if (normalizedLocation.equals("south")) return "southeast";
            return normalizedLocation;
        }

        return "southeast";
    }
}

package com.example.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Service that calls a trained scikit-learn model via Python subprocess.
 * The model predicts insurance costs based on user input features.
 */
@Service
public class MLPredictionService {

    private static final String PYTHON_SCRIPT_PATH = "src/main/resources/predict_model.py";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Predict insurance cost using the trained ML model.
     * @param age age of the person
     * @param gender gender (male/female/other)
     * @param bmi body mass index
     * @param kids number of kids
     * @param smoker whether the person is a smoker
     * @param location location (e.g., northeast, southeast, southwest, northwest)
     * @param income income level (low, medium, high, very_high)
     * @param employment employment status (employed, self_employed, unemployed, retired)
     * @param healthScore health score 1-10
     * @param exerciseFrequency exercise frequency (0-7 days per week)
     * @param education education level (high_school, bachelor, master, phd)
     * @param maritalStatus marital status (single, married, divorced, widowed)
     * @param yearsInsured years with insurance
     * @param modelName model to use (random_forest, decision_tree, linear_regression). If null, uses default.
     * @return map containing model name and predicted cost
     */
    public Map<String, Object> predictInsuranceCost(int age, String gender, double bmi, int kids, boolean smoker, 
            String location, String income, String employment, int healthScore, int exerciseFrequency,
            String education, String maritalStatus, int yearsInsured, String modelName) {
        try {
            // Convert boolean smoker to yes/no (model was trained with these values)
            String smokerStr = smoker ? "yes" : "no";
            
            // Normalize inputs to lowercase
            location = location.toLowerCase();
            income = income.toLowerCase();
            employment = employment.toLowerCase();
            education = education.toLowerCase();
            maritalStatus = maritalStatus.toLowerCase();
            
            // Build the Python command with arguments (using key=value format for clarity)
            String pythonExecutable = getPythonExecutable();
            ProcessBuilder pb;
            
            // Use key=value format to pass all parameters clearly
            if (modelName != null && !modelName.trim().isEmpty()) {
                pb = new ProcessBuilder(
                        pythonExecutable,
                        PYTHON_SCRIPT_PATH,
                        "age=" + String.valueOf(age),
                        "gender=" + gender.toLowerCase(),
                        "bmi=" + String.valueOf(bmi),
                        "kids=" + String.valueOf(kids),
                        "smoker=" + smokerStr,
                        "location=" + location,
                        "income=" + income,
                        "employment=" + employment,
                        "health_score=" + String.valueOf(healthScore),
                        "exercise_frequency=" + String.valueOf(exerciseFrequency),
                        "education=" + education,
                        "marital_status=" + maritalStatus,
                        "years_insured=" + String.valueOf(yearsInsured),
                        "--model",
                        modelName
                );
            } else {
                pb = new ProcessBuilder(
                        pythonExecutable,
                        PYTHON_SCRIPT_PATH,
                        "age=" + String.valueOf(age),
                        "gender=" + gender.toLowerCase(),
                        "bmi=" + String.valueOf(bmi),
                        "kids=" + String.valueOf(kids),
                        "smoker=" + smokerStr,
                        "location=" + location,
                        "income=" + income,
                        "employment=" + employment,
                        "health_score=" + String.valueOf(healthScore),
                        "exercise_frequency=" + String.valueOf(exerciseFrequency),
                        "education=" + education,
                        "marital_status=" + maritalStatus,
                        "years_insured=" + String.valueOf(yearsInsured)
                );
            }

            // Set working directory to project root so Python script can find models/
            pb.directory(new File(System.getProperty("user.dir")));
            pb.redirectErrorStream(false);  // Keep separate streams for better error handling
            Process process = pb.start();

            // Read output from both streams
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();
            String line;
            
            // Read stdout (prediction result)
            while ((line = stdoutReader.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.contains("UserWarning") && !line.contains("FutureWarning")) {
                    output.append(line).append("\n");
                }
            }
            
            // Read stderr (errors and warnings)
            while ((line = stderrReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            
            if (exitCode != 0 || !errorOutput.toString().trim().isEmpty()) {
                String allOutput = "Exit code: " + exitCode + "\nStdout: " + output + "\nStderr: " + errorOutput;
                System.err.println("[ML Service] Python script output: " + allOutput);
                if (exitCode != 0) {
                    Map<String, Object> errorMap = new HashMap<>();
                    errorMap.put("model", modelName != null ? modelName : "unknown");
                    errorMap.put("prediction", 0.0);
                    return errorMap;
                }
            }

            // Parse the prediction from Python output (now in JSON format)
            String result = output.toString().trim();
            
            if (result.isEmpty()) {
                System.err.println("[ML Service] Empty prediction output. Full output: " + output + " | Error: " + errorOutput);
                Map<String, Object> errorMap = new HashMap<>();
                errorMap.put("model", modelName != null ? modelName : "unknown");
                errorMap.put("prediction", 0.0);
                return errorMap;
            }
            
            try {
                // Try to parse as JSON first
                Map<String, Object> jsonResult = objectMapper.readValue(result, Map.class);
                double prediction = ((Number) jsonResult.get("prediction")).doubleValue();
                String usedModel = (String) jsonResult.getOrDefault("model", modelName != null ? modelName : "random_forest");
                
                System.out.println("[ML Service] Successfully predicted: $" + prediction + " using " + usedModel + 
                    " | age=" + age + ", gender=" + gender + ", bmi=" + bmi + ", kids=" + kids + 
                    ", smoker=" + smokerStr + ", location=" + location + ", income=" + income + 
                    ", employment=" + employment + ", health_score=" + healthScore + 
                    ", exercise=" + exerciseFrequency + ", education=" + education + 
                    ", marital=" + maritalStatus + ", years_insured=" + yearsInsured);
                
                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("model", usedModel);
                responseMap.put("prediction", prediction);
                return responseMap;
            } catch (Exception jsonEx) {
                // Fallback: try to parse as plain number (for backward compatibility)
                try {
                    double prediction = Double.parseDouble(result);
                    System.out.println("[ML Service] Successfully predicted (legacy format): $" + prediction);
                    Map<String, Object> responseMap = new HashMap<>();
                    responseMap.put("model", modelName != null ? modelName : "random_forest");
                    responseMap.put("prediction", prediction);
                    return responseMap;
                } catch (NumberFormatException e) {
                    System.err.println("[ML Service] Failed to parse prediction: '" + result + "' | Error: " + e.getMessage());
                    Map<String, Object> errorMap = new HashMap<>();
                    errorMap.put("model", modelName != null ? modelName : "unknown");
                    errorMap.put("prediction", 0.0);
                    return errorMap;
                }
            }
        } catch (Exception e) {
            System.err.println("[ML Service] Error calling ML model: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("model", modelName != null ? modelName : "unknown");
            errorMap.put("prediction", 0.0);
            return errorMap;
        }
    }

    /**
     * Get the Python executable path. Tries to use the venv if available, otherwise uses system python.
     */
    private String getPythonExecutable() {
        // Try to use venv Python first
        String[] pythonPaths = {
                "C:/Users/ASIF EBRAHIM/Downloads/demo/.venv/Scripts/python.exe",
                ".venv/Scripts/python.exe",
                "python",
                "python3"
        };

        for (String path : pythonPaths) {
            try {
                ProcessBuilder pb = new ProcessBuilder(path, "--version");
                pb.redirectErrorStream(true);
                Process p = pb.start();
                int code = p.waitFor();
                if (code == 0) {
                    return path;
                }
            } catch (Exception ignored) {}
        }

        // Default fallback
        return "python";
    }
}

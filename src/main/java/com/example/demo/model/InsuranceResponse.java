package com.example.demo.model;

public class InsuranceResponse {
    private String message;
    private Double prediction;
    private String model;

    public InsuranceResponse() {}

    public InsuranceResponse(String message, Double prediction, String model) {
        this.message = message;
        this.prediction = prediction;
        this.model = model;
    }
    public InsuranceResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Double getPrediction() { return prediction; }
    public void setPrediction(Double prediction) { this.prediction = prediction; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
}

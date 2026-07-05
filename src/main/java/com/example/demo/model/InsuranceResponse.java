package com.example.demo.model;

public class InsuranceResponse {
    private String message;
    private Double prediction;

    public InsuranceResponse() {}

    public InsuranceResponse(String message, Double prediction) {
        this.message = message;
        this.prediction = prediction;
    }
    public InsuranceResponse(String message) {
        this.message = message;
    }
    public InsuranceResponse(Double prediction){
        this.prediction = prediction;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Double getPrediction() { return prediction; }
    public void setPrediction(Double prediction) { this.prediction = prediction; }
}

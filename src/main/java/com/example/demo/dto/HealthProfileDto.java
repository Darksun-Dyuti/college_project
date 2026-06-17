package com.example.demo.dto;

public class HealthProfileDto {
    private Long id;
    private boolean hasDiabetes;
    private boolean hasHypertension;
    private boolean hasHeartDisease;
    private boolean hasAsthma;
    private boolean hasCancer;
    private boolean hasKidneyDisease;
    private boolean hasArthritis;
    private String otherConditions;
    
    // Constructors
    public HealthProfileDto() {
    }
    
    public HealthProfileDto(Long id, boolean hasDiabetes, boolean hasHypertension, 
                           boolean hasHeartDisease, boolean hasAsthma, boolean hasCancer,
                           boolean hasKidneyDisease, boolean hasArthritis, String otherConditions) {
        this.id = id;
        this.hasDiabetes = hasDiabetes;
        this.hasHypertension = hasHypertension;
        this.hasHeartDisease = hasHeartDisease;
        this.hasAsthma = hasAsthma;
        this.hasCancer = hasCancer;
        this.hasKidneyDisease = hasKidneyDisease;
        this.hasArthritis = hasArthritis;
        this.otherConditions = otherConditions;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public boolean isHasDiabetes() {
        return hasDiabetes;
    }
    
    public void setHasDiabetes(boolean hasDiabetes) {
        this.hasDiabetes = hasDiabetes;
    }
    
    public boolean isHasHypertension() {
        return hasHypertension;
    }
    
    public void setHasHypertension(boolean hasHypertension) {
        this.hasHypertension = hasHypertension;
    }
    
    public boolean isHasHeartDisease() {
        return hasHeartDisease;
    }
    
    public void setHasHeartDisease(boolean hasHeartDisease) {
        this.hasHeartDisease = hasHeartDisease;
    }
    
    public boolean isHasAsthma() {
        return hasAsthma;
    }
    
    public void setHasAsthma(boolean hasAsthma) {
        this.hasAsthma = hasAsthma;
    }
    
    public boolean isHasCancer() {
        return hasCancer;
    }
    
    public void setHasCancer(boolean hasCancer) {
        this.hasCancer = hasCancer;
    }
    
    public boolean isHasKidneyDisease() {
        return hasKidneyDisease;
    }
    
    public void setHasKidneyDisease(boolean hasKidneyDisease) {
        this.hasKidneyDisease = hasKidneyDisease;
    }
    
    public boolean isHasArthritis() {
        return hasArthritis;
    }
    
    public void setHasArthritis(boolean hasArthritis) {
        this.hasArthritis = hasArthritis;
    }
    
    public String getOtherConditions() {
        return otherConditions;
    }
    
    public void setOtherConditions(String otherConditions) {
        this.otherConditions = otherConditions;
    }
}

package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "predictions")
public class Prediction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    private Double estimatedCost;
    private String model;
    private Integer age;
    private String gender;
    private Double bmi;
    private Integer kids;
    private Boolean smoker;
    private String location;
    private String income;
    private String employment;
    private Integer healthScore;
    private Integer exerciseFrequency;
    private String education;
    private String maritalStatus;
    private Integer yearsInsured;
    
    @Column(columnDefinition = "LONGTEXT")
    private String fullResponse;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Prediction() {}
    
    public Prediction(User user, Double estimatedCost, String model) {
        this.user = user;
        this.estimatedCost = estimatedCost;
        this.model = model;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Double getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(Double estimatedCost) { this.estimatedCost = estimatedCost; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }
    
    public Integer getKids() { return kids; }
    public void setKids(Integer kids) { this.kids = kids; }
    
    public Boolean getSmoker() { return smoker; }
    public void setSmoker(Boolean smoker) { this.smoker = smoker; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getIncome() { return income; }
    public void setIncome(String income) { this.income = income; }
    
    public String getEmployment() { return employment; }
    public void setEmployment(String employment) { this.employment = employment; }
    
    public Integer getHealthScore() { return healthScore; }
    public void setHealthScore(Integer healthScore) { this.healthScore = healthScore; }
    
    public Integer getExerciseFrequency() { return exerciseFrequency; }
    public void setExerciseFrequency(Integer exerciseFrequency) { this.exerciseFrequency = exerciseFrequency; }
    
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    
    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }
    
    public Integer getYearsInsured() { return yearsInsured; }
    public void setYearsInsured(Integer yearsInsured) { this.yearsInsured = yearsInsured; }
    
    public String getFullResponse() { return fullResponse; }
    public void setFullResponse(String fullResponse) { this.fullResponse = fullResponse; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

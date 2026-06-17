package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_profiles")
public class HealthProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private boolean hasDiabetes = false;
    
    @Column(nullable = false)
    private boolean hasHypertension = false;
    
    @Column(nullable = false)
    private boolean hasHeartDisease = false;
    
    @Column(nullable = false)
    private boolean hasAsthma = false;
    
    @Column(nullable = false)
    private boolean hasCancer = false;
    
    @Column(nullable = false)
    private boolean hasKidneyDisease = false;
    
    @Column(nullable = false)
    private boolean hasArthritis = false;
    
    @Column(length = 500)
    private String otherConditions;
    
    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime updatedAt;
    
    // Constructors
    public HealthProfile() {
    }
    
    public HealthProfile(User user) {
        this.user = user;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

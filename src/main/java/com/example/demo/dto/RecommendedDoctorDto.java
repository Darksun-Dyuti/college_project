package com.example.demo.dto;

import java.io.Serializable;

public class RecommendedDoctorDto implements Serializable {
    private Long id;
    private String fullName;
    private String specialization;
    private String qualification;
    private String hospitalName;
    private String phoneNumber;
    private String email;
    private double rating;
    private String experience;
    private String reason;
    
    // Constructors
    public RecommendedDoctorDto() {
    }
    
    public RecommendedDoctorDto(Long id, String fullName, String specialization, String qualification,
                               String hospitalName, String phoneNumber, String email, double rating,
                               String experience, String reason) {
        this.id = id;
        this.fullName = fullName;
        this.specialization = specialization;
        this.qualification = qualification;
        this.hospitalName = hospitalName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.rating = rating;
        this.experience = experience;
        this.reason = reason;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getQualification() {
        return qualification;
    }
    
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    
    public String getHospitalName() {
        return hospitalName;
    }
    
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public String getExperience() {
        return experience;
    }
    
    public void setExperience(String experience) {
        this.experience = experience;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
}

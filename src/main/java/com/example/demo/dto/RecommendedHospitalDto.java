package com.example.demo.dto;

import java.io.Serializable;

public class RecommendedHospitalDto implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String state;
    private String phoneNumber;
    private String email;
    private String specializations;
    private double rating;
    private String website;
    private String reason;
    
    // Constructors
    public RecommendedHospitalDto() {
    }
    
    public RecommendedHospitalDto(Long id, String name, String address, String city, String state,
                                 String phoneNumber, String email, String specializations,
                                 double rating, String website, String reason) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.specializations = specializations;
        this.rating = rating;
        this.website = website;
        this.reason = reason;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(String state) {
        this.state = state;
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
    
    public String getSpecializations() {
        return specializations;
    }
    
    public void setSpecializations(String specializations) {
        this.specializations = specializations;
    }
    
    public double getRating() {
        return rating;
    }
    
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
}

package com.example.demo.model;

public class InsuranceRequest {
    private String name;
    private Integer age;
    private String gender;
    private Boolean smoker;
    private String location;
    private Integer kids;
    private Double bmi;
    private String existingCondition;
    private String model; // Model selection: random_forest, decision_tree, linear_regression
    
    // New features
    private String income;  // low, medium, high, very_high
    private String employment;  // employed, self_employed, unemployed, retired
    private Integer healthScore;  // 1-10 scale
    private Integer exerciseFrequency;  // days per week (0-7)
    private String education;  // high_school, bachelor, master, phd
    private String maritalStatus;  // single, married, divorced, widowed
    private Integer yearsInsured;  // years with insurance (0-50)

    public InsuranceRequest() {}

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public Boolean getSmoker() { return smoker; }
    public void setSmoker(Boolean smoker) { this.smoker = smoker; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getKids() { return kids; }
    public void setKids(Integer kids) { this.kids = kids; }

    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }

    public String getExistingCondition() { return existingCondition; }
    public void setExistingCondition(String existingCondition) { this.existingCondition = existingCondition; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

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
}


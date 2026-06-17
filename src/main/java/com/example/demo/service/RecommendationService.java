package com.example.demo.service;

import com.example.demo.dto.HealthProfileDto;
import com.example.demo.dto.RecommendedDoctorDto;
import com.example.demo.dto.RecommendedHospitalDto;
import com.example.demo.entity.Doctor;
import com.example.demo.entity.HealthProfile;
import com.example.demo.entity.Hospital;
import com.example.demo.entity.User;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HealthProfileRepository;
import com.example.demo.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecommendationService {
    
    @Autowired
    private HealthProfileRepository healthProfileRepository;
    
    @Autowired
    private HospitalRepository hospitalRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    // Map conditions to specializations
    private static final Map<String, String[]> CONDITION_SPECIALIZATION_MAP = new HashMap<>();
    
    static {
        CONDITION_SPECIALIZATION_MAP.put("diabetes", new String[]{"Endocrinology", "Internal Medicine", "Cardiology"});
        CONDITION_SPECIALIZATION_MAP.put("hypertension", new String[]{"Cardiology", "Internal Medicine", "Nephrology"});
        CONDITION_SPECIALIZATION_MAP.put("heart_disease", new String[]{"Cardiology", "Cardiac Surgery", "Internal Medicine"});
        CONDITION_SPECIALIZATION_MAP.put("asthma", new String[]{"Pulmonology", "Allergy and Immunology", "Internal Medicine"});
        CONDITION_SPECIALIZATION_MAP.put("cancer", new String[]{"Oncology", "Surgical Oncology", "Radiation Oncology"});
        CONDITION_SPECIALIZATION_MAP.put("kidney_disease", new String[]{"Nephrology", "Internal Medicine", "Urology"});
        CONDITION_SPECIALIZATION_MAP.put("arthritis", new String[]{"Rheumatology", "Orthopedics", "Internal Medicine"});
    }
    
    // Get or create health profile for user
    public HealthProfile getOrCreateHealthProfile(User user) {
        return healthProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    HealthProfile newProfile = new HealthProfile(user);
                    return healthProfileRepository.save(newProfile);
                });
    }
    
    // Update health profile
    public HealthProfile updateHealthProfile(User user, HealthProfileDto dto) {
        HealthProfile profile = getOrCreateHealthProfile(user);
        profile.setHasDiabetes(dto.isHasDiabetes());
        profile.setHasHypertension(dto.isHasHypertension());
        profile.setHasHeartDisease(dto.isHasHeartDisease());
        profile.setHasAsthma(dto.isHasAsthma());
        profile.setHasCancer(dto.isHasCancer());
        profile.setHasKidneyDisease(dto.isHasKidneyDisease());
        profile.setHasArthritis(dto.isHasArthritis());
        profile.setOtherConditions(dto.getOtherConditions());
        profile.setUpdatedAt(LocalDateTime.now());
        return healthProfileRepository.save(profile);
    }
    
    // Convert to DTO
    public HealthProfileDto convertToDto(HealthProfile profile) {
        return new HealthProfileDto(
                profile.getId(),
                profile.isHasDiabetes(),
                profile.isHasHypertension(),
                profile.isHasHeartDisease(),
                profile.isHasAsthma(),
                profile.isHasCancer(),
                profile.isHasKidneyDisease(),
                profile.isHasArthritis(),
                profile.getOtherConditions()
        );
    }
    
    // Get recommended specialists based on conditions
    public List<String> getRecommendedSpecializations(HealthProfile profile) {
        Set<String> specializations = new HashSet<>();
        
        if (profile.isHasDiabetes()) {
            specializations.addAll(Arrays.asList(CONDITION_SPECIALIZATION_MAP.get("diabetes")));
        }
        if (profile.isHasHypertension()) {
            specializations.addAll(Arrays.asList(CONDITION_SPECIALIZATION_MAP.get("hypertension")));
        }
        if (profile.isHasHeartDisease()) {
            specializations.addAll(Arrays.asList(CONDITION_SPECIALIZATION_MAP.get("heart_disease")));
        }
        if (profile.isHasAsthma()) {
            specializations.addAll(Arrays.asList(CONDITION_SPECIALIZATION_MAP.get("asthma")));
        }
        if (profile.isHasCancer()) {
            specializations.addAll(Arrays.asList(CONDITION_SPECIALIZATION_MAP.get("cancer")));
        }
        if (profile.isHasKidneyDisease()) {
            specializations.addAll(Arrays.asList(CONDITION_SPECIALIZATION_MAP.get("kidney_disease")));
        }
        if (profile.isHasArthritis()) {
            specializations.addAll(Arrays.asList(CONDITION_SPECIALIZATION_MAP.get("arthritis")));
        }
        
        return new ArrayList<>(specializations);
    }
    
    // Get reason for recommendation
    private String getRecommendationReason(HealthProfile profile, String specialization) {
        List<String> reasons = new ArrayList<>();
        
        if (profile.isHasDiabetes() && containsSpecialization("diabetes", specialization)) {
            reasons.add("Diabetes management");
        }
        if (profile.isHasHypertension() && containsSpecialization("hypertension", specialization)) {
            reasons.add("Blood pressure control");
        }
        if (profile.isHasHeartDisease() && containsSpecialization("heart_disease", specialization)) {
            reasons.add("Heart disease care");
        }
        if (profile.isHasAsthma() && containsSpecialization("asthma", specialization)) {
            reasons.add("Respiratory care");
        }
        if (profile.isHasCancer() && containsSpecialization("cancer", specialization)) {
            reasons.add("Cancer treatment");
        }
        if (profile.isHasKidneyDisease() && containsSpecialization("kidney_disease", specialization)) {
            reasons.add("Kidney disease management");
        }
        if (profile.isHasArthritis() && containsSpecialization("arthritis", specialization)) {
            reasons.add("Joint and bone care");
        }
        
        return String.join(", ", reasons);
    }
    
    private boolean containsSpecialization(String condition, String specialization) {
        String[] specs = CONDITION_SPECIALIZATION_MAP.get(condition);
        if (specs != null) {
            for (String spec : specs) {
                if (specialization.contains(spec)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    // Get recommended hospitals
    public List<RecommendedHospitalDto> getRecommendedHospitals(User user) {
        HealthProfile profile = getOrCreateHealthProfile(user);
        
        if (!hasAnyCondition(profile)) {
            return new ArrayList<>();
        }
        
        List<String> specializations = getRecommendedSpecializations(profile);
        Map<Long, RecommendedHospitalDto> hospitalMap = new LinkedHashMap<>();
        
        for (String specialization : specializations) {
            List<Hospital> hospitals = hospitalRepository.findBySpecialization(specialization);
            
            for (Hospital hospital : hospitals) {
                if (!hospitalMap.containsKey(hospital.getId())) {
                    RecommendedHospitalDto dto = new RecommendedHospitalDto(
                            hospital.getId(),
                            hospital.getName(),
                            hospital.getAddress(),
                            hospital.getCity(),
                            hospital.getState(),
                            hospital.getPhoneNumber(),
                            hospital.getEmail(),
                            hospital.getSpecializations(),
                            hospital.getRating(),
                            hospital.getWebsite(),
                            getRecommendationReason(profile, hospital.getSpecializations())
                    );
                    hospitalMap.put(hospital.getId(), dto);
                } else {
                    // Append reason if already exists
                    RecommendedHospitalDto existing = hospitalMap.get(hospital.getId());
                    existing.setReason(existing.getReason() + ", " + getRecommendationReason(profile, hospital.getSpecializations()));
                }
            }
        }
        
        return hospitalMap.values().stream()
                .sorted((h1, h2) -> Double.compare(h2.getRating(), h1.getRating()))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    // Get recommended doctors
    public List<RecommendedDoctorDto> getRecommendedDoctors(User user) {
        HealthProfile profile = getOrCreateHealthProfile(user);
        
        if (!hasAnyCondition(profile)) {
            return new ArrayList<>();
        }
        
        List<String> specializations = getRecommendedSpecializations(profile);
        Map<Long, RecommendedDoctorDto> doctorMap = new LinkedHashMap<>();
        
        for (String specialization : specializations) {
            List<Doctor> doctors = doctorRepository.findBySpecializationOrderByRating(specialization);
            
            for (Doctor doctor : doctors) {
                if (!doctorMap.containsKey(doctor.getId())) {
                    RecommendedDoctorDto dto = new RecommendedDoctorDto(
                            doctor.getId(),
                            doctor.getFullName(),
                            doctor.getSpecialization(),
                            doctor.getQualification(),
                            doctor.getHospital().getName(),
                            doctor.getPhoneNumber(),
                            doctor.getEmail(),
                            doctor.getRating(),
                            doctor.getExperience(),
                            getRecommendationReason(profile, doctor.getSpecialization())
                    );
                    doctorMap.put(doctor.getId(), dto);
                }
            }
        }
        
        return doctorMap.values().stream()
                .sorted((d1, d2) -> Double.compare(d2.getRating(), d1.getRating()))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    // Check if user has any health condition
    private boolean hasAnyCondition(HealthProfile profile) {
        return profile.isHasDiabetes() || profile.isHasHypertension() || profile.isHasHeartDisease() ||
               profile.isHasAsthma() || profile.isHasCancer() || profile.isHasKidneyDisease() ||
               profile.isHasArthritis();
    }
}

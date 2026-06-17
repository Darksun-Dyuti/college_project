package com.example.demo.service;

import com.example.demo.dto.HealthProfileDto;
import com.example.demo.entity.HealthProfile;
import com.example.demo.entity.User;
import com.example.demo.repository.HealthProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class HealthProfileService {
    
    @Autowired
    private HealthProfileRepository healthProfileRepository;
    
    public HealthProfile getHealthProfile(User user) {
        return healthProfileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    HealthProfile newProfile = new HealthProfile(user);
                    return healthProfileRepository.save(newProfile);
                });
    }
    
    public HealthProfileDto getHealthProfileDto(User user) {
        HealthProfile profile = getHealthProfile(user);
        return convertToDto(profile);
    }
    
    public HealthProfile updateHealthProfile(User user, HealthProfileDto dto) {
        HealthProfile profile = getHealthProfile(user);
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
}

package com.example.demo.config;

import com.example.demo.entity.Doctor;
import com.example.demo.entity.Hospital;
import com.example.demo.repository.DoctorRepository;
import com.example.demo.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SampleDataLoader {
    
    @Autowired
    private HospitalRepository hospitalRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;
    
    @Bean
    public CommandLineRunner loadSampleData() {
        return args -> {
            // Check if hospitals already exist
            if (hospitalRepository.count() == 0) {
                // Create sample hospitals
                Hospital hospital1 = new Hospital(
                    "Sunshine Medical Center",
                    "123 Healthcare Boulevard",
                    "New York",
                    "NY",
                    "10001",
                    "555-0001",
                    "Cardiology, Endocrinology, Nephrology"
                );
                hospital1.setRating(4.8);
                hospital1.setEmail("contact@sunshinemedical.com");
                hospital1.setWebsite("www.sunshinemedical.com");
                
                Hospital hospital2 = new Hospital(
                    "Heart Care Hospital",
                    "456 Cardiac Lane",
                    "Boston",
                    "MA",
                    "02101",
                    "555-0002",
                    "Cardiology, Cardiac Surgery, Internal Medicine"
                );
                hospital2.setRating(4.9);
                hospital2.setEmail("info@heartcarehospital.com");
                hospital2.setWebsite("www.heartcarehospital.com");
                
                Hospital hospital3 = new Hospital(
                    "Wellness & Care Hospital",
                    "789 Health Street",
                    "Chicago",
                    "IL",
                    "60601",
                    "555-0003",
                    "Pulmonology, Oncology, Rheumatology"
                );
                hospital3.setRating(4.7);
                hospital3.setEmail("support@wellnesscare.com");
                hospital3.setWebsite("www.wellnesscare.com");
                
                Hospital hospital4 = new Hospital(
                    "Advanced Medical Institute",
                    "321 Science Park",
                    "Houston",
                    "TX",
                    "77001",
                    "555-0004",
                    "Orthopedics, Allergy and Immunology, Internal Medicine"
                );
                hospital4.setRating(4.6);
                hospital4.setEmail("contact@advancedmedical.com");
                
                hospitalRepository.save(hospital1);
                hospitalRepository.save(hospital2);
                hospitalRepository.save(hospital3);
                hospitalRepository.save(hospital4);
                
                // Create sample doctors
                Doctor doctor1 = new Doctor(
                    "Dr. Sarah Johnson",
                    "Cardiology",
                    "LIC-001",
                    "MD, Board Certified in Cardiology",
                    hospital1,
                    "555-1001"
                );
                doctor1.setRating(4.9);
                doctor1.setEmail("dr.johnson@sunshinemedical.com");
                doctor1.setExperience("15+ years in Cardiology");
                
                Doctor doctor2 = new Doctor(
                    "Dr. Michael Chen",
                    "Endocrinology",
                    "LIC-002",
                    "MD, Fellowship in Endocrinology",
                    hospital1,
                    "555-1002"
                );
                doctor2.setRating(4.8);
                doctor2.setEmail("dr.chen@sunshinemedical.com");
                doctor2.setExperience("12+ years in Diabetes Management");
                
                Doctor doctor3 = new Doctor(
                    "Dr. Robert Williams",
                    "Cardiology",
                    "LIC-003",
                    "MD, Cardiac Surgery Specialist",
                    hospital2,
                    "555-1003"
                );
                doctor3.setRating(4.9);
                doctor3.setEmail("dr.williams@heartcarehospital.com");
                doctor3.setExperience("20+ years in Cardiac Surgery");
                
                Doctor doctor4 = new Doctor(
                    "Dr. Emily Davis",
                    "Internal Medicine",
                    "LIC-004",
                    "MD, General Internal Medicine",
                    hospital2,
                    "555-1004"
                );
                doctor4.setRating(4.7);
                doctor4.setEmail("dr.davis@heartcarehospital.com");
                doctor4.setExperience("10+ years");
                
                Doctor doctor5 = new Doctor(
                    "Dr. James Martinez",
                    "Pulmonology",
                    "LIC-005",
                    "MD, Board Certified in Pulmonary Medicine",
                    hospital3,
                    "555-1005"
                );
                doctor5.setRating(4.8);
                doctor5.setEmail("dr.martinez@wellnesscare.com");
                doctor5.setExperience("14+ years in Respiratory Care");
                
                Doctor doctor6 = new Doctor(
                    "Dr. Lisa Anderson",
                    "Oncology",
                    "LIC-006",
                    "MD, Fellowship in Medical Oncology",
                    hospital3,
                    "555-1006"
                );
                doctor6.setRating(4.8);
                doctor6.setEmail("dr.anderson@wellnesscare.com");
                doctor6.setExperience("13+ years in Cancer Treatment");
                
                Doctor doctor7 = new Doctor(
                    "Dr. David Thompson",
                    "Rheumatology",
                    "LIC-007",
                    "MD, Board Certified in Rheumatology",
                    hospital3,
                    "555-1007"
                );
                doctor7.setRating(4.7);
                doctor7.setEmail("dr.thompson@wellnesscare.com");
                doctor7.setExperience("11+ years in Joint and Bone Care");
                
                Doctor doctor8 = new Doctor(
                    "Dr. Jennifer Lee",
                    "Orthopedics",
                    "LIC-008",
                    "MD, Orthopedic Surgery Specialist",
                    hospital4,
                    "555-1008"
                );
                doctor8.setRating(4.7);
                doctor8.setEmail("dr.lee@advancedmedical.com");
                doctor8.setExperience("12+ years in Orthopedic Surgery");
                
                Doctor doctor9 = new Doctor(
                    "Dr. Christopher Brown",
                    "Nephrology",
                    "LIC-009",
                    "MD, Fellowship in Nephrology",
                    hospital1,
                    "555-1009"
                );
                doctor9.setRating(4.8);
                doctor9.setEmail("dr.brown@sunshinemedical.com");
                doctor9.setExperience("11+ years in Kidney Care");
                
                Doctor doctor10 = new Doctor(
                    "Dr. Patricia Garcia",
                    "Allergy and Immunology",
                    "LIC-010",
                    "MD, Board Certified in Allergy/Immunology",
                    hospital4,
                    "555-1010"
                );
                doctor10.setRating(4.6);
                doctor10.setEmail("dr.garcia@advancedmedical.com");
                doctor10.setExperience("9+ years in Allergy Management");
                
                doctorRepository.save(doctor1);
                doctorRepository.save(doctor2);
                doctorRepository.save(doctor3);
                doctorRepository.save(doctor4);
                doctorRepository.save(doctor5);
                doctorRepository.save(doctor6);
                doctorRepository.save(doctor7);
                doctorRepository.save(doctor8);
                doctorRepository.save(doctor9);
                doctorRepository.save(doctor10);
                
                System.out.println("Sample data loaded successfully!");
            }
        };
    }
}

package com.example.demo.repository;

import com.example.demo.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialization(String specialization);
    
    List<Doctor> findByHospitalId(Long hospitalId);
    
    @Query("SELECT d FROM Doctor d WHERE " +
           "d.specialization LIKE CONCAT('%', :specialization, '%') " +
           "AND d.hospital.id = :hospitalId " +
           "ORDER BY d.rating DESC")
    List<Doctor> findBySpecializationAndHospital(@Param("specialization") String specialization, 
                                                  @Param("hospitalId") Long hospitalId);
    
    @Query("SELECT d FROM Doctor d WHERE " +
           "d.specialization LIKE CONCAT('%', :specialization, '%') " +
           "ORDER BY d.rating DESC")
    List<Doctor> findBySpecializationOrderByRating(@Param("specialization") String specialization);
}

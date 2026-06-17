package com.example.demo.repository;

import com.example.demo.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {
    List<Hospital> findByCity(String city);
    
    List<Hospital> findByState(String state);
    
    @Query("SELECT h FROM Hospital h WHERE " +
           "h.specializations LIKE CONCAT('%', :specialization, '%') " +
           "ORDER BY h.rating DESC")
    List<Hospital> findBySpecialization(@Param("specialization") String specialization);
}

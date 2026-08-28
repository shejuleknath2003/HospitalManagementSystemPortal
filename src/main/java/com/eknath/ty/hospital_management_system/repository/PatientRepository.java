package com.eknath.ty.hospital_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eknath.ty.hospital_management_system.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer>{
	
	List<Patient> findByName(String name);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}

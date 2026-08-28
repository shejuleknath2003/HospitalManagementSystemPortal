package com.eknath.ty.hospital_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eknath.ty.hospital_management_system.entity.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Integer>{
	
	 //Search doctors by specialization
	   // View doctors by department

	    List<Doctor> findByDepartmentDepartmentName(String departmentName);
	    Doctor findByEmail(String email);
	    boolean existsByEmail(String email);

}

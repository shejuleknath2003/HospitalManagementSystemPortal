package com.eknath.ty.hospital_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eknath.ty.hospital_management_system.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer>{
	
	 boolean existsByDepartmentName(String departmentName);

}

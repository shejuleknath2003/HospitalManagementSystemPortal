package com.eknath.ty.hospital_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eknath.ty.hospital_management_system.entity.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer>{
	
	List<Appointment> findByDoctorId(int id);
	
	List<Appointment> findByPatientId(int id);
	
	boolean existsById(int id);

}

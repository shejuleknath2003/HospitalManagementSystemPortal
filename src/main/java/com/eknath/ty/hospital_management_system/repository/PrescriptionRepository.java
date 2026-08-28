package com.eknath.ty.hospital_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eknath.ty.hospital_management_system.entity.Appointment;
import com.eknath.ty.hospital_management_system.entity.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Integer>{
	
	boolean existsByAppointment(Appointment appointment);

}

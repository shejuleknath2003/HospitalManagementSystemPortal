package com.eknath.ty.hospital_management_system.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public class AppointmentRequestDTO {
	
	@NotNull(message="Id is required")
	private int id;
	@FutureOrPresent(message="appointment date can't be past")
	private LocalDate appointmentDate;
	@FutureOrPresent(message="appointment Time can't be past")
	private LocalTime appointmentTime;
	@NotNull(message="Patient Id is required")
	private int patientId;
	@NotNull(message="Doctor Id is required")
	private int doctorId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public LocalTime getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(LocalTime appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public int getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}
	
	

}

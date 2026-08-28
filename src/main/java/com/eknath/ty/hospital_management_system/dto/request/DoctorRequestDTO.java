package com.eknath.ty.hospital_management_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DoctorRequestDTO {
	
	@NotNull(message="Id is required")
	private int id;
	@NotBlank(message="Name is required")
	private String name;
	@Email(message="Email Should be in abc@gmail.com format")
	private String email;
	@NotBlank(message="Specialization is required")
	private String specialization;
	@Positive(message="Experience should be positive")
	private Integer experience;
	@Positive(message="Fee should be positive")
	private  Double consultationFee;
	@NotNull(message="Department Id is required")
	private int departmentId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public Integer getExperience() {
		return experience;
	}
	public void setExperience(Integer experience) {
		this.experience = experience;
	}
	public Double getConsultationFee() {
		return consultationFee;
	}
	public void setConsultationFee(Double consultationFee) {
		this.consultationFee = consultationFee;
	}
	public int getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(int departmentId) {
		this.departmentId = departmentId;
	}
	

}

package com.eknath.ty.hospital_management_system.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class PatientRequestDTO {
	
	@NotNull(message="Id is required")
	private int id;
	@NotBlank(message="Name should not be empty")
	private String name;
	@Email(message="Email should be int abc@gmail.com format")
	private String email;
	@Size(max=10)
	private String phone;
	@Positive(message="Age should be positive")
	private int age;
	@NotBlank(message="Gender is required")
	private String gender;
	@NotBlank(message="Address is required")
	private String address;
	
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	

}

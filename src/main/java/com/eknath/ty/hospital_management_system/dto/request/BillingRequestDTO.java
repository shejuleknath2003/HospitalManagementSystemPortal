package com.eknath.ty.hospital_management_system.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BillingRequestDTO {
	
	@NotNull(message="Id is required")
	private int id;
	@Positive(message="Amount should be Positive")
	private double amount;
	@NotNull(message="Patient Id is required")
	private int patientId;
	@NotNull(message="Payment method is required")
	private String paymentMethod;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getPatientId() {
		return patientId;
	}
	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	
	
	

}

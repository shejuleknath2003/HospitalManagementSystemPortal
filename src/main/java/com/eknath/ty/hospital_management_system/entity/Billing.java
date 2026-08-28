package com.eknath.ty.hospital_management_system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Billing {
	
	 @Id
	 private int id;
	 private double amount;
	 private String paymentMethod;
	 @ManyToOne
	 private Patient patient;
	 
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
	 public String getPaymentMethod() {
		 return paymentMethod;
	 }
	 public void setPaymentMethod(String paymentMethod) {
		 this.paymentMethod = paymentMethod;
	 }
	 public Patient getPatient() {
		 return patient;
	 }
	 public void setPatient(Patient patient) {
		 this.patient = patient;
	 }
	 
	 

}

package com.eknath.ty.hospital_management_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PrescriptionRequestDTO {
	
	@NotNull(message="Id is required")
	private int id;
	@NotBlank(message="Diagnosis is required")
	private String diagnosis;
	@NotBlank(message="Medicines are amndatory")
	private String medicines;
	@NotBlank(message="Instruction are required")
	private String instructions;
	@NotNull(message="Appointment Id is required")
	private int appointmentId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public String getMedicines() {
		return medicines;
	}
	public void setMedicines(String medicines) {
		this.medicines = medicines;
	}
	public String getInstructions() {
		return instructions;
	}
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	public int getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}
	
	

}

package com.eknath.ty.hospital_management_system.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eknath.ty.hospital_management_system.dto.request.AppointmentRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.AppointmentResponseDTO;
import com.eknath.ty.hospital_management_system.service.AppointmentService;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/appointment") 
public class AppointmentController {
	
	@Autowired
	private AppointmentService appointmentService;
	
	@PostMapping("/save")
	public ResponseEntity<ResponseStructure<AppointmentResponseDTO>> bookAppointment(@Valid @RequestBody AppointmentRequestDTO appointmentRequest){
		appointmentRequest.setAppointmentDate(LocalDate.now());
		appointmentRequest.setAppointmentTime(LocalTime.now());
		return appointmentService.saveAppointment(appointmentRequest); 
	}
	
	@PostMapping("/saveAll")
	public ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>> bookAllAppointment(@Valid @RequestBody List<AppointmentRequestDTO> appointmentRequest){
		for(AppointmentRequestDTO appointmentRequestDTO : appointmentRequest) {
			appointmentRequestDTO.setAppointmentDate(LocalDate.now());
			appointmentRequestDTO.setAppointmentTime(LocalTime.now());
		}
		
		return appointmentService.saveAllAppintments(appointmentRequest); 
	}
	
	@GetMapping("/viewId/{id}")
	public ResponseEntity<ResponseStructure<AppointmentResponseDTO>> viewAppointment(@PathVariable int id){
		return appointmentService.findAppointment(id);
	}
	
	@GetMapping("/viewAll") 
	public ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>> viewAppAppointments(){
		return appointmentService.findAllAppointments();
	}
	
	@PutMapping("/update")
	public ResponseEntity<ResponseStructure<AppointmentResponseDTO>> updateAppointment(@Valid @RequestBody AppointmentRequestDTO appointmentRequest){
		appointmentRequest.setAppointmentDate(LocalDate.now());
		appointmentRequest.setAppointmentTime(LocalTime.now());
		return appointmentService.updateAppointment(appointmentRequest);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<ResponseStructure<String>> deleteAppointment(@PathVariable int id){
		return appointmentService.deleteAppointment(id);
	}
	
	@GetMapping("viewByDoctor/{doctorId}")
	public ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>> viewByDoctor(@PathVariable int doctorId){
		return appointmentService.findByDoctor(doctorId);
	}
	
	@GetMapping("viewByPatient/{patientId}")
	public ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>> viewByPatient(@PathVariable int patientId){
		return appointmentService.findByPrtient(patientId);
	}
	

}

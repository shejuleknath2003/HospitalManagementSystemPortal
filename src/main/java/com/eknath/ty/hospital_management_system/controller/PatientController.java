package com.eknath.ty.hospital_management_system.controller;

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

import com.eknath.ty.hospital_management_system.dto.request.PatientRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.PatientResponseDTO;
import com.eknath.ty.hospital_management_system.exception.ResourceAlreadyExistException;
import com.eknath.ty.hospital_management_system.service.PatientService;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/patient")
public class PatientController {
	
	 @Autowired
	    private PatientService patientService;

	    @PostMapping("/save")
	    public ResponseEntity<ResponseStructure<PatientResponseDTO>> registerPatient(@Valid @RequestBody PatientRequestDTO patient) throws ResourceAlreadyExistException {
	        return patientService.savePatient(patient);
	    }
	    @PostMapping("/saveAll")
	    public ResponseEntity<ResponseStructure<List<PatientResponseDTO>>> registerAllPatients(@Valid @RequestBody List<PatientRequestDTO> patients) throws ResourceAlreadyExistException{
	        return patientService.saveAllPatients(patients);
	    }
	    @GetMapping("/viewById/{id}")
	    public ResponseEntity<ResponseStructure<PatientResponseDTO>> viewPatient(@PathVariable int id){
	        return patientService.findById(id);
	    }
	    @GetMapping("/viewAll")
	    public ResponseEntity<ResponseStructure<List<PatientResponseDTO>>> viewAllPatients(){
	        return patientService.findAllPatients();
	    }
	    @PutMapping("/update")
	    public ResponseEntity<ResponseStructure<PatientResponseDTO>> updatePatient(@Valid @RequestBody PatientRequestDTO patient) throws ResourceAlreadyExistException{
	        return patientService.updatePatient(patient);
	    }
	    @DeleteMapping("/delete/{id}")
	    public ResponseEntity<ResponseStructure<String>> deletePatient(@PathVariable int id){
	        return patientService.deletePatient(id);
	    }
	    @GetMapping("/viewByName/{name}")
	    public ResponseEntity<ResponseStructure<List<PatientResponseDTO>>> viewPatientByName(@PathVariable String name){
	        return patientService.findByName(name);
	    }

}

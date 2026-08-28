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

import com.eknath.ty.hospital_management_system.dto.request.PrescriptionRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.PrescriptionResponseDTO;
import com.eknath.ty.hospital_management_system.exception.ResourceAlreadyExistException;
import com.eknath.ty.hospital_management_system.exception.ResourceNotFoundException;
import com.eknath.ty.hospital_management_system.service.PrescriptionService;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/prescription")
public class PrescriptionController {
	
	@Autowired
    private PrescriptionService prescriptionService;

    @PostMapping("/save")
    public ResponseEntity<ResponseStructure<PrescriptionResponseDTO>> addPrescription(@Valid @RequestBody PrescriptionRequestDTO prescription) throws ResourceNotFoundException, ResourceAlreadyExistException {
        return prescriptionService.savePrescription(prescription);
    }
    @PutMapping("/update")
    public ResponseEntity<ResponseStructure<PrescriptionResponseDTO>> updatePrescription(@Valid @RequestBody PrescriptionRequestDTO prescription){
        return prescriptionService.updatePrescription(prescription);
    }
    @GetMapping("/view/{id}")
    public ResponseEntity<ResponseStructure<PrescriptionResponseDTO>> viewPrescription(@PathVariable int id) throws ResourceNotFoundException {
        return prescriptionService.findPrescription(id);
    }
    @GetMapping("/viewAll")
    public ResponseEntity<ResponseStructure<List<PrescriptionResponseDTO>>> viewAllPrescriptions() {
        return prescriptionService.findAllPrescriptions();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseStructure<String>> deletePrescription(@PathVariable int id){
        return prescriptionService.deletePrescription(id);
    }
}

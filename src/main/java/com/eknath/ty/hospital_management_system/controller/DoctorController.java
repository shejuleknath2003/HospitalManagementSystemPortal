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

import com.eknath.ty.hospital_management_system.dto.request.DoctorRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.DoctorResponseDTO;
import com.eknath.ty.hospital_management_system.exception.ResourceAlreadyExistException;
import com.eknath.ty.hospital_management_system.service.DoctorService;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/doctor")
public class DoctorController {
	
	@Autowired
    public DoctorService doctorService;

    @PostMapping("/save")
    public ResponseEntity<ResponseStructure<DoctorResponseDTO>> addDoctor(@Valid @RequestBody DoctorRequestDTO doctor) throws ResourceAlreadyExistException {
        return doctorService.saveDoctor(doctor);
    }
    @PostMapping("/saveAll")
    public ResponseEntity<ResponseStructure<List<DoctorResponseDTO>>> addAllDoctors(@Valid @RequestBody List<DoctorRequestDTO> doctors){
        return doctorService.saveAllDoctors(doctors);
    }
    @GetMapping("/viewById/{id}")
    public ResponseEntity<ResponseStructure<DoctorResponseDTO>> viewDoctor(@PathVariable int id){
        return doctorService.findDoctor(id);
    }
    @GetMapping("/viewAll")
    public ResponseEntity<ResponseStructure<List<DoctorResponseDTO>>> viewAllDoctors(){
        return doctorService.findAllDoctors();
    }
    @PutMapping("/update")
    public ResponseEntity<ResponseStructure<DoctorResponseDTO>> updateDoctor(@Valid @RequestBody DoctorRequestDTO doctor){
        return doctorService.updateDoctor(doctor);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteDoctor(@PathVariable int id){
        return doctorService.deleteDoctor(id);
    }
    @GetMapping("/viewByEmail/{email}")
    public ResponseEntity<ResponseStructure<DoctorResponseDTO>> viewDoctorByEmail(@PathVariable String email){
        return doctorService.findByEmail(email);
    }
    @GetMapping("/viewByDepartment/{departmentName}")
    public ResponseEntity<ResponseStructure<List<DoctorResponseDTO>>> viewDoctorByDepartment(@PathVariable String departmentName){
        return doctorService.findDoctorByDepartment(departmentName);
    }

}

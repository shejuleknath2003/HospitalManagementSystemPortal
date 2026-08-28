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

import com.eknath.ty.hospital_management_system.dto.request.DepartmentRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.DepartmentResponseDTO;
import com.eknath.ty.hospital_management_system.exception.ResourceAlreadyExistException;
import com.eknath.ty.hospital_management_system.repository.DepartmentRepository;
import com.eknath.ty.hospital_management_system.service.DepartmentService;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;


import jakarta.validation.Valid;

@RestController
@RequestMapping("/department")
public class DepartmentController {
	
	@Autowired
    private DepartmentService departmentService;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping("/save")
    public ResponseEntity<ResponseStructure<DepartmentResponseDTO>> addDepartment(@Valid @RequestBody DepartmentRequestDTO department) throws ResourceAlreadyExistException {
        System.out.println(department.getId());
    	return departmentService.saveDepartment(department);
    }
    @PostMapping("/saveAll")
    public ResponseEntity<ResponseStructure<List<DepartmentResponseDTO>>> addAllDepartments(@Valid @RequestBody List<DepartmentRequestDTO> departments) throws ResourceAlreadyExistException{
        return departmentService.saveAllDepartments(departments);
    }
    @GetMapping("/viewAll")
    public ResponseEntity<ResponseStructure<List<DepartmentResponseDTO>>> viewAllDepartments(){
        return departmentService.findAllDepartments();
    }
    @GetMapping("/view/{id}")
    public ResponseEntity<ResponseStructure<DepartmentResponseDTO>> viewDepartment(@PathVariable int id){
        return departmentService.findDepartment(id);
    }
    @PutMapping("/update")
    public ResponseEntity<ResponseStructure<DepartmentResponseDTO>> updateDepartment(@Valid @RequestBody DepartmentRequestDTO department) throws ResourceAlreadyExistException{
        return departmentService.updateDepartment(department);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseStructure<String>> deleteDepartment(@PathVariable int id){
        return departmentService.deleteDepartment(id);
    }

}

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

import com.eknath.ty.hospital_management_system.dto.request.BillingRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.BillingResponseDTO;
import com.eknath.ty.hospital_management_system.service.BillingService;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/billing")
public class BillingController {
	
	 @Autowired
	    private BillingService billingService;

	    @PostMapping("/save")
	    public ResponseEntity<ResponseStructure<BillingResponseDTO>> generateBill(@Valid @RequestBody BillingRequestDTO billing){
	        return billingService.saveBill(billing);
	    }
	    @PutMapping("/update")
	    public ResponseEntity<ResponseStructure<BillingResponseDTO>> updateBill(@Valid @RequestBody BillingRequestDTO billing){
	        return billingService.updateBill(billing);
	    }
	    @GetMapping("/view/{id}")
	    public ResponseEntity<ResponseStructure<BillingResponseDTO>> viewBill(@PathVariable int id){
	        return billingService.findBill(id);
	    }
	    @GetMapping("/viewAll")
	    public ResponseEntity<ResponseStructure<List<BillingResponseDTO>>> viewAllBills(){
	        return billingService.findAllBills();
	    }
	    @DeleteMapping("/delete/{id}")
	    public ResponseEntity<ResponseStructure<String>> deleteBill(@PathVariable int id){
	        return billingService.deleteBill(id);
	    }

}

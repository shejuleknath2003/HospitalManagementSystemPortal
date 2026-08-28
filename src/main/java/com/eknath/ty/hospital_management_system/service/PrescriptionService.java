package com.eknath.ty.hospital_management_system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eknath.ty.hospital_management_system.dto.request.PrescriptionRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.PrescriptionResponseDTO;
import com.eknath.ty.hospital_management_system.entity.Appointment;
import com.eknath.ty.hospital_management_system.entity.Prescription;
import com.eknath.ty.hospital_management_system.exception.ResourceAlreadyExistException;
import com.eknath.ty.hospital_management_system.exception.ResourceNotFoundException;
import com.eknath.ty.hospital_management_system.repository.AppointmentRepository;
import com.eknath.ty.hospital_management_system.repository.PrescriptionRepository;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;

@Service
public class PrescriptionService {

	@Autowired
	private PrescriptionRepository prescriptionRepository;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	//Add prescription
    public ResponseEntity<ResponseStructure<PrescriptionResponseDTO>> savePrescription(PrescriptionRequestDTO prescription) throws ResourceAlreadyExistException,ResourceNotFoundException{
        Prescription prescription1=new Prescription();
        prescription1.setId(prescription.getId());
        prescription1.setDiagnosis(prescription.getDiagnosis());
        prescription1.setInstructions(prescription.getInstructions());
        prescription1.setMedicines(prescription.getMedicines());
        Optional<Appointment> appointment=appointmentRepository.findById(prescription.getAppointmentId());
        if(appointment.isPresent()){
            if(!(prescriptionRepository.existsByAppointment(appointment.get()))){
                prescription1.setAppointment(appointment.get());
            }
            else {
                throw new ResourceAlreadyExistException("Duplicate appointment");
            }
        }
        else{
            throw new ResourceNotFoundException("Appointment not found");
        }
        Prescription savedPrescription=prescriptionRepository.save(prescription1);
        return mapToResponse(savedPrescription);
    }

    public ResponseEntity<ResponseStructure<PrescriptionResponseDTO>> mapToResponse(Prescription savedPrescription) {
        PrescriptionResponseDTO dto=new PrescriptionResponseDTO();
        dto.setId(savedPrescription.getId());
        dto.setDiagnosis(savedPrescription.getDiagnosis());
        dto.setMedicines(savedPrescription.getMedicines());
        dto.setInstructions(savedPrescription.getInstructions());
        if (savedPrescription.getAppointment() != null) {
            dto.setAppointmentDate(savedPrescription.getAppointment().getAppointmentDate());
            dto.setAppointmentId(savedPrescription.getAppointment().getId());
        }
        ResponseStructure<PrescriptionResponseDTO> rs=new ResponseStructure<>();
        rs.setStatusCode(HttpStatus.OK.value());
        rs.setMessage("Success");
        rs.setData(dto);
        return new ResponseEntity<ResponseStructure<PrescriptionResponseDTO>>(rs, HttpStatus.OK);
    }

    //update prescription
    public ResponseEntity<ResponseStructure<PrescriptionResponseDTO>> updatePrescription(PrescriptionRequestDTO prescription){
        Prescription prescription1=new Prescription();
        prescription1.setId(prescription.getId());
        prescription1.setDiagnosis(prescription.getDiagnosis());
        prescription1.setInstructions(prescription.getInstructions());
        prescription1.setMedicines(prescription.getMedicines());
        Appointment appointment=appointmentRepository.findById(prescription.getAppointmentId()).orElse(null);
        prescription1.setAppointment(appointment);
        Prescription savedPrescription=prescriptionRepository.save(prescription1);
        return mapToResponse(savedPrescription);
    }
    //delete prescription
    public ResponseEntity<ResponseStructure<String>> deletePrescription(int id){
        ResponseStructure<String > rs=new ResponseStructure<>();
        Optional<Prescription> optional = prescriptionRepository.findById(id);
        if(optional.isPresent()) {
            rs.setStatusCode(HttpStatus.OK.value());
            rs.setMessage("Success");
            prescriptionRepository.deleteById(id);
            rs.setData("Prescription deleted succesfully");
        }
        else {
            throw new ResourceNotFoundException("Prescription doesn't exists");
        }
        return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.OK);
    }
    //fetch prescription by id
    public ResponseEntity<ResponseStructure<PrescriptionResponseDTO>> findPrescription(int id) throws ResourceNotFoundException {
        Optional<Prescription> optional=prescriptionRepository.findById(id);
        if(optional.isPresent()){
            return mapToResponse(optional.get());
        }
        else{
            throw new ResourceNotFoundException("Prescription Not exists");
        }
    }

    //fetch all prescriptions
    public ResponseEntity<ResponseStructure<List<PrescriptionResponseDTO>>> findAllPrescriptions() {
        List<Prescription> list = prescriptionRepository.findAll();
        List<PrescriptionResponseDTO> dtoList = new ArrayList<>();
        for (Prescription p : list) {
            PrescriptionResponseDTO dto = new PrescriptionResponseDTO();
            dto.setId(p.getId());
            dto.setDiagnosis(p.getDiagnosis());
            dto.setMedicines(p.getMedicines());
            dto.setInstructions(p.getInstructions());
            if (p.getAppointment() != null) {
                dto.setAppointmentDate(p.getAppointment().getAppointmentDate());
                dto.setAppointmentId(p.getAppointment().getId());
            }
            dtoList.add(dto);
        }
        ResponseStructure<List<PrescriptionResponseDTO>> rs = new ResponseStructure<>();
        rs.setStatusCode(HttpStatus.OK.value());
        rs.setMessage("Success");
        rs.setData(dtoList);
        return new ResponseEntity<ResponseStructure<List<PrescriptionResponseDTO>>>(rs, HttpStatus.OK);
    }
	
}

package com.eknath.ty.hospital_management_system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eknath.ty.hospital_management_system.dto.request.PatientRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.PatientResponseDTO;
import com.eknath.ty.hospital_management_system.entity.Patient;
import com.eknath.ty.hospital_management_system.exception.ResourceAlreadyExistException;
import com.eknath.ty.hospital_management_system.exception.ResourceNotFoundException;
import com.eknath.ty.hospital_management_system.repository.PatientRepository;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;

@Service
public class PatientService {
	
	@Autowired
	private PatientRepository patientRepository;
	
	//Save a Patient
    public ResponseEntity<ResponseStructure<PatientResponseDTO>> savePatient(PatientRequestDTO patient) throws ResourceAlreadyExistException{
        Patient patient1=new Patient();
        patient1.setId(patient.getId());
        patient1.setName(patient.getName());
        if(!(patientRepository.existsByEmail(patient.getEmail()))) {
            patient1.setEmail(patient.getEmail());
        }
        else{
            throw new ResourceAlreadyExistException("Email already exists");
        }
        patient1.setAddress(patient.getAddress());
        patient1.setAge(patient.getAge());
        patient1.setGender(patient.getGender());
        if((!(patientRepository.existsByPhone(patient.getPhone())))) {
            patient1.setPhone(patient.getPhone());
        }
        else{
            throw new ResourceAlreadyExistException("Phone number already exists");
        }
        Patient savedPatient=patientRepository.save(patient1);
        return mapToResponse(savedPatient);
    }

    public ResponseEntity<ResponseStructure<PatientResponseDTO>> mapToResponse(Patient savedPatient) {
        PatientResponseDTO patientResponseDTO=new PatientResponseDTO();
        patientResponseDTO.setId(savedPatient.getId());
        patientResponseDTO.setName(savedPatient.getName());
        patientResponseDTO.setEmail(savedPatient.getEmail());
        patientResponseDTO.setPhone(savedPatient.getPhone());
        patientResponseDTO.setAge(savedPatient.getAge());
        patientResponseDTO.setGender(savedPatient.getGender());
        patientResponseDTO.setAddress(savedPatient.getAddress());
        ResponseStructure<PatientResponseDTO> rs=new ResponseStructure<>();
        rs.setStatusCode(HttpStatus.OK.value());
        rs.setMessage("Success");
        rs.setData(patientResponseDTO);
        return new ResponseEntity<ResponseStructure<PatientResponseDTO>>(rs,HttpStatus.OK);
    }

    //Save All Patients
    public ResponseEntity<ResponseStructure<List<PatientResponseDTO>>> saveAllPatients(List<PatientRequestDTO> patients) throws ResourceAlreadyExistException{
        List<Patient> list=new ArrayList<>();
        for(PatientRequestDTO dto:patients){
            Patient patient=new Patient();
            patient.setId(dto.getId());
            patient.setName(dto.getName());
            patient.setAge(dto.getAge());
            if(!(patientRepository.existsByEmail(dto.getEmail()))) {
                patient.setEmail(dto.getEmail());
            }
            else{
                throw new ResourceAlreadyExistException("Email already exists");
            }
            patient.setAddress(dto.getAddress());
            patient.setGender(dto.getGender());
            if(!(patientRepository.existsByPhone(dto.getPhone()))) {
                patient.setPhone(dto.getPhone());
            }
            else{
                throw new ResourceAlreadyExistException("Phone number already exists");
            }
            list.add(patient);
        }
        List<Patient> savedPatients= patientRepository.saveAll(list);
        return mapToResponseList(savedPatients);
    }

    public ResponseEntity<ResponseStructure<List<PatientResponseDTO>>> mapToResponseList(List<Patient> savedPatients) {
        List<PatientResponseDTO> list=new ArrayList<>();
        for(Patient patient:savedPatients){
            PatientResponseDTO dto=new PatientResponseDTO();
            dto.setId(patient.getId());
            dto.setName(patient.getName());
            dto.setEmail(patient.getEmail());
            dto.setPhone(patient.getPhone());
            dto.setAge(patient.getAge());
            dto.setGender(patient.getGender());
            dto.setAddress(patient.getAddress());
            list.add(dto);
        }
        ResponseStructure<List<PatientResponseDTO>> rs=new ResponseStructure<>();
        rs.setStatusCode(HttpStatus.OK.value());
        rs.setMessage("Success");
        rs.setData(list);
        return new ResponseEntity<ResponseStructure<List<PatientResponseDTO>>>(rs,HttpStatus.OK);
    }

    //Update Patient
    public ResponseEntity<ResponseStructure<PatientResponseDTO>> updatePatient(PatientRequestDTO patient) throws ResourceAlreadyExistException{
        Patient patient1=new Patient();
        patient1.setId(patient.getId());
        patient1.setName(patient.getName());
        if(patientRepository.existsByEmail(patient.getEmail())) {
            patient1.setEmail(patient.getEmail());
        }
        else{
            throw new ResourceAlreadyExistException("Email already exists");
        }
        patient1.setAddress(patient.getAddress());
        patient1.setAge(patient.getAge());
        patient1.setGender(patient.getGender());
        if(!(patientRepository.existsByPhone(patient.getPhone()))) {
            patient1.setPhone(patient.getPhone());
        }
        else{
            throw new ResourceAlreadyExistException("Phone number already exists");
        }
        Patient savedPatient= patientRepository.save(patient1);
        return mapToResponse(savedPatient);
    }

    //Delete Patient by id
    public ResponseEntity<ResponseStructure<String>> deletePatient(int id){
        ResponseStructure<String> rs=new ResponseStructure<>();
        rs.setStatusCode(200);
        rs.setMessage("Deleted");
        Optional<Patient> optional = patientRepository.findById(id);
        if(optional.isPresent()) {
            patientRepository.deleteById(id);
            rs.setData("Patient Deleted Succesfully");
        }
        else
            rs.setData("Patient doesn't exists");
        return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.OK);
    }

    //Find Patient By id
    public ResponseEntity<ResponseStructure<PatientResponseDTO>> findById(int id){
        Optional<Patient> patient  =patientRepository.findById(id);
        if(patient.isPresent()){
            return mapToResponse(patient.get());
        }
        else{
            throw new ResourceNotFoundException("Patient not found");
        }
    }

    //Find all Patients
    public ResponseEntity<ResponseStructure<List<PatientResponseDTO>>> findAllPatients(){
        List<Patient> list=patientRepository.findAll();
        return mapToResponseList(list);
    }

    //Find Patient by name
    public ResponseEntity<ResponseStructure<List<PatientResponseDTO>>> findByName(String name){
        List<Patient> patient= patientRepository.findByName(name);
        if(!(patient.isEmpty())){
            return mapToResponseList(patient);
        }
       else
           throw new ResourceNotFoundException("Patient with name : "+name+" not found");
    }

}

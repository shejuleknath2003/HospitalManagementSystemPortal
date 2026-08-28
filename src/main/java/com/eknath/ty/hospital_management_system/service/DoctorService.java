package com.eknath.ty.hospital_management_system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eknath.ty.hospital_management_system.dto.request.DoctorRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.DoctorResponseDTO;
import com.eknath.ty.hospital_management_system.entity.Department;
import com.eknath.ty.hospital_management_system.entity.Doctor;
import com.eknath.ty.hospital_management_system.exception.ResourceAlreadyExistException;
import com.eknath.ty.hospital_management_system.exception.ResourceNotFoundException;
import com.eknath.ty.hospital_management_system.repository.DepartmentRepository;
import com.eknath.ty.hospital_management_system.repository.DoctorRepository;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;


@Service
public class DoctorService {
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private DepartmentRepository departmentRepository;
	
	 //Save a doctor
    public ResponseEntity<ResponseStructure<DoctorResponseDTO>> saveDoctor(DoctorRequestDTO doctor) throws ResourceAlreadyExistException {
        Doctor doctor1=new Doctor();
        doctor1.setId(doctor.getId());
        doctor1.setName(doctor.getName());
        if(!(doctorRepository.existsByEmail(doctor.getEmail()))) {
            doctor1.setEmail(doctor.getEmail());
        }
        else{
            throw new ResourceAlreadyExistException("Doctor Email already exists");
        }
        doctor1.setExperience(doctor.getExperience());
        doctor1.setSpecialization(doctor.getSpecialization());
        doctor1.setConsultationFee(doctor.getConsultationFee());
        Department department=departmentRepository.findById(doctor.getDepartmentId()).orElse(null);
        doctor1.setDepartment(department);
        Doctor savedDoctor=doctorRepository.save(doctor1);
        return mapToResponseDTO(savedDoctor);
    }

    public ResponseEntity<ResponseStructure<DoctorResponseDTO>> mapToResponseDTO(Doctor savedDoctor) {
        DoctorResponseDTO dto=new DoctorResponseDTO();
        dto.setId(savedDoctor.getId());
        dto.setName(savedDoctor.getName());
        dto.setEmail(savedDoctor.getEmail());
        dto.setSpecialization(savedDoctor.getSpecialization());
        dto.setExperience(savedDoctor.getExperience());
        dto.setConsultationFee(savedDoctor.getConsultationFee());
        if (savedDoctor.getDepartment() != null) {
            dto.setDepartmentName(savedDoctor.getDepartment().getDepartmentName());
            dto.setDepartmentId(savedDoctor.getDepartment().getId());
        }
        ResponseStructure<DoctorResponseDTO> rs=new ResponseStructure<>();
        rs.setStatusCode(HttpStatus.OK.value());
        rs.setMessage("Success");
        rs.setData(dto);
        return new ResponseEntity<ResponseStructure<DoctorResponseDTO>>(rs, HttpStatus.OK);
    }

    //Save All doctors
    public ResponseEntity<ResponseStructure<List<DoctorResponseDTO>>> saveAllDoctors(List<DoctorRequestDTO> doctors){
        List<Doctor> dto=new ArrayList<>();
        for(DoctorRequestDTO dto1:doctors) {
            Doctor doctor = new Doctor();
            doctor.setId(dto1.getId());
            doctor.setName(dto1.getName());
            doctor.setEmail(dto1.getEmail());
            doctor.setExperience(dto1.getExperience());
            doctor.setSpecialization(dto1.getSpecialization());
            doctor.setConsultationFee(dto1.getConsultationFee());
            doctor.setDepartment(departmentRepository.findById(dto1.getDepartmentId()).orElse(null));
            dto.add(doctor);
        }
        List<Doctor> savedDoctors =doctorRepository.saveAll(dto);
        return mapToResponseDTOList(savedDoctors);
    }

    public ResponseEntity<ResponseStructure<List<DoctorResponseDTO>>> mapToResponseDTOList(List<Doctor> savedDoctors) {
        List<DoctorResponseDTO> list=new ArrayList<>();
        for(Doctor d:savedDoctors){
            DoctorResponseDTO dto=new DoctorResponseDTO();
            dto.setId(d.getId());
            dto.setName(d.getName());
            dto.setEmail(d.getEmail());
            dto.setSpecialization(d.getSpecialization());
            dto.setExperience(d.getExperience());
            dto.setConsultationFee(d.getConsultationFee());
            if (d.getDepartment() != null) {
                dto.setDepartmentName(d.getDepartment().getDepartmentName());
                dto.setDepartmentId(d.getDepartment().getId());
            }
            list.add(dto);
        }
        ResponseStructure<List<DoctorResponseDTO>> rs= new ResponseStructure<>();
        rs.setStatusCode(HttpStatus.OK.value());
        rs.setMessage("Success");
        rs.setData(list);
        return new ResponseEntity<ResponseStructure<List<DoctorResponseDTO>>>(rs,HttpStatus.OK);
    }

    //Update Doctor
    public ResponseEntity<ResponseStructure<DoctorResponseDTO>> updateDoctor(DoctorRequestDTO doctor) {
        Doctor doctor1=new Doctor();
        doctor1.setId(doctor.getId());
        doctor1.setName(doctor.getName());
        doctor1.setEmail(doctor.getEmail());
        doctor1.setExperience(doctor.getExperience());
        doctor1.setSpecialization(doctor.getSpecialization());
        doctor1.setConsultationFee(doctor.getConsultationFee());
        doctor1.setDepartment(departmentRepository.findById(doctor.getDepartmentId()).orElse(null));
        return mapToResponseDTO(doctorRepository.save(doctor1));
    }

    //Delete Doctor by id
    public ResponseEntity<ResponseStructure<String>> deleteDoctor(int id){
        ResponseStructure<String> rs=new ResponseStructure<>();
        Optional<Doctor> optional = doctorRepository.findById(id);
        if(optional.isPresent()){
            doctorRepository.deleteById(id);
            rs.setStatusCode(HttpStatus.OK.value());
            rs.setMessage("Deleted");
            rs.setData("Doctor deleted succesfully");
        }
        else
            throw new ResourceNotFoundException("Doctor not found");
        return new ResponseEntity<ResponseStructure<String >>(rs,HttpStatus.OK);
    }

    //Find Doctor by id
    public ResponseEntity<ResponseStructure<DoctorResponseDTO>> findDoctor(int id){
        Optional<Doctor> doctor=doctorRepository.findById(id);
        if(doctor.isPresent()){
            return mapToResponseDTO(doctor.get());
        }
        else{
            throw new ResourceNotFoundException("Doctor not found");
        }
    }

    //Find All Doctors
    public ResponseEntity<ResponseStructure<List<DoctorResponseDTO>>> findAllDoctors(){
        List<Doctor> savedDoctors=doctorRepository.findAll();
        return mapToResponseDTOList(savedDoctors);
    }

    //Find doctor by Department
    public ResponseEntity<ResponseStructure<List<DoctorResponseDTO>>> findDoctorByDepartment(String departmentName){
        List<Doctor> doctors=doctorRepository.findByDepartmentDepartmentName(departmentName);
        if(!(doctors.isEmpty())) {
            return mapToResponseDTOList(doctors);
        }
        else
            throw new ResourceNotFoundException("Doctors with department "+departmentName+" not found");
    }

    //Find doctor by Email
    public ResponseEntity<ResponseStructure<DoctorResponseDTO>> findByEmail(String email){
        Doctor doctor=doctorRepository.findByEmail(email);
        if(doctor!=null) {
            return mapToResponseDTO(doctor);
        }
        else{
            throw new ResourceNotFoundException("Doctor with email "+email+" not found");
        }
    }

}

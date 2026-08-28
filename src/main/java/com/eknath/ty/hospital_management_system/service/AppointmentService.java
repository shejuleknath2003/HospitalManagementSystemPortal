package com.eknath.ty.hospital_management_system.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.eknath.ty.hospital_management_system.dto.request.AppointmentRequestDTO;
import com.eknath.ty.hospital_management_system.dto.response.AppointmentResponseDTO;
import com.eknath.ty.hospital_management_system.entity.Appointment;
import com.eknath.ty.hospital_management_system.entity.Doctor;
import com.eknath.ty.hospital_management_system.entity.Patient;
import com.eknath.ty.hospital_management_system.exception.ResourceNotFoundException;
import com.eknath.ty.hospital_management_system.repository.AppointmentRepository;
import com.eknath.ty.hospital_management_system.repository.DoctorRepository;
import com.eknath.ty.hospital_management_system.repository.PatientRepository;
import com.eknath.ty.hospital_management_system.utility.ResponseStructure;


@Service
public class AppointmentService {
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private PatientRepository patientRepository;
	
	//saveAppointment
	public ResponseEntity<ResponseStructure<AppointmentResponseDTO>> saveAppointment(AppointmentRequestDTO appointment){
		//Creating appointment entity object and saving inside the database
		 Optional<Doctor> doctor=doctorRepository.findById(appointment.getDoctorId());
	     Optional<Patient> patient=patientRepository.findById(appointment.getPatientId());
	     Appointment appointment1 = new Appointment();
	     appointment1.setId(appointment.getId());
	     appointment1.setAppointmentDate(appointment.getAppointmentDate());
	     appointment1.setAppointmentTime(appointment.getAppointmentTime());
	     doctor.ifPresentOrElse(
	    		    appointment1::setDoctor,
	    		    () -> {
	    		        throw new ResourceNotFoundException(
	    		            "Doctor With Id " + appointment.getDoctorId() + " Not Found"
	    		        );
	    		    }
	    		);
		patient.ifPresentOrElse(
				appointment1::setPatient,
				() -> {
					throw new ResourceNotFoundException(
							"Patient With Id " + appointment.getPatientId() + " Not Found"
					);
				}
		);
		//Saving inside the database
		Appointment appointment2 = appointmentRepository.save(appointment1);
		//Converting entity into responseDTO
		return mapToResponseDTO(appointment2);

	     
	}

	//Save All Appointments
	public ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>>  saveAllAppintments(List<AppointmentRequestDTO> appointment){
		//Creating list of appointment object
		List<Appointment> appointments = new ArrayList<>();
		for(AppointmentRequestDTO a : appointment){
			Optional<Doctor> doctor = doctorRepository.findById(a.getDoctorId());
			Optional<Patient> patient = patientRepository.findById((a.getPatientId()));
			Appointment appointment1 = new Appointment();
			appointment1.setId(a.getId());
			appointment1.setAppointmentDate(a.getAppointmentDate());
			appointment1.setAppointmentTime(a.getAppointmentTime());
			doctor.ifPresentOrElse(
					appointment1::setDoctor,
					() -> {
						throw new ResourceNotFoundException(
								"Doctor With Id " + a.getDoctorId() + " Not Found"
						);
					}
			);
			patient.ifPresentOrElse(
					appointment1::setPatient,
					() -> {
						throw new ResourceNotFoundException(
								"Patient With Id " + a.getPatientId() + " Not Found"
						);
					}
			);
			appointments.add(appointment1);

		}
		//Saving list of appointment objects
		List<Appointment> appointmentsList = appointmentRepository.saveAll(appointments);
		//Converting list of entity into reponseDTO
		return mapToResponseDTOList(appointmentsList);

	}
    //Upadte Appointment
	public ResponseEntity<ResponseStructure<AppointmentResponseDTO>> updateAppointment(AppointmentRequestDTO appointment){
		Appointment appointment1 = new Appointment();
		if(appointmentRepository.existsById(appointment.getId())){
			appointment1.setId(appointment.getId());
			appointment1.setAppointmentDate(appointment.getAppointmentDate());
			appointment1.setAppointmentTime(appointment.getAppointmentTime());
 			Optional<Doctor> doctor = doctorRepository.findById(appointment.getDoctorId());
			Optional<Patient> patient = patientRepository.findById((appointment.getPatientId()));
			doctor.ifPresentOrElse(
					appointment1::setDoctor,
					() -> {
						throw new ResourceNotFoundException(
								"Doctor With Id " + appointment.getDoctorId() + " Not Found"
						);
					}
			);
			patient.ifPresentOrElse(
					appointment1::setPatient,
					() -> {
						throw new ResourceNotFoundException(
								"Patient With Id " + appointment.getPatientId() + " Not Found"
						);
					}
			);

		}
		else{
			throw new ResourceNotFoundException("Appointment with id "+appointment.getId()+" not found");
		}
		Appointment saved = appointmentRepository.save(appointment1);
		return mapToResponseDTO(saved);
	}
	
    //Delete Appointment by id
	public ResponseEntity<ResponseStructure<String>> deleteAppointment(int id){
		Optional<Appointment> optional = appointmentRepository.findById(id);
		ResponseStructure<String> rs = new ResponseStructure<String>();
		if(optional.isPresent()) {
			rs.setStatusCode(HttpStatus.OK.value());
			rs.setMessage("Deleted Successfully...!");
			appointmentRepository.deleteById(id);
			rs.setData("Appointment Deleted Successfully..!!");
		} 
		else {
			throw new ResourceNotFoundException("Appointment with id " + id + "not found");
		}
		return new ResponseEntity<ResponseStructure<String>>(rs,HttpStatus.OK);
	}
	
    //Find Appointment by Id
	public ResponseEntity<ResponseStructure<AppointmentResponseDTO>> findAppointment(int id){
		Optional<Appointment> optional = appointmentRepository.findById(id);
		if(optional.isPresent()) {
			return mapToResponseDTO(optional.get());
		}
		else {
			throw new ResourceNotFoundException("Appointment with id " + id + "not found");
		}
	}
	
    //Find All Appointments
	public ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>> findAllAppointments(){
		List<Appointment> list = appointmentRepository.findAll();
		return mapToResponseDTOList(list); 
	}
	
    //Find by doctor id
	public ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>> findByDoctor(int id){
		List<Appointment> appointments = appointmentRepository.findByDoctorId(id);
		if(!(appointments.isEmpty())) {
			return mapToResponseDTOList(appointments);
		}
		else {
			throw new ResourceNotFoundException("Doctor with Id : " + id + " not found"); 
		}
		
	}
	
    //Find by Patient id
	public ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>> findByPrtient(int id){
		List<Appointment> appointments = appointmentRepository.findByPatientId(id);
		if(!(appointments.isEmpty())) {
			return mapToResponseDTOList(appointments);
		}
		else {
			throw new ResourceNotFoundException("Patient with Id : " + id + " not found"); 
		}
		
	}

	//Entity object to ResponseStructure Object
	public ResponseEntity<ResponseStructure<AppointmentResponseDTO>> mapToResponseDTO(Appointment appointment) {
		AppointmentResponseDTO appointmentResponseDTO =  new AppointmentResponseDTO();
		appointmentResponseDTO.setId(appointment.getId());
		appointmentResponseDTO.setAppointmentDate(appointment.getAppointmentDate());
		appointmentResponseDTO.setAppointmentTime(appointment.getAppointmentTime());
		if (appointment.getDoctor() != null) {
			appointmentResponseDTO.setDoctorId(appointment.getDoctor().getId());
		}
		if (appointment.getPatient() != null) {
			appointmentResponseDTO.setPatientId(appointment.getPatient().getId());
		}
		ResponseStructure<AppointmentResponseDTO> rs = new ResponseStructure<>();
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("Successfully saved");
		rs.setData(appointmentResponseDTO);

		return new ResponseEntity<ResponseStructure<AppointmentResponseDTO>>(rs,HttpStatus.OK);
	}


	//List of entity object to responseStructure object
	public ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>> mapToResponseDTOList(List<Appointment> appointmentsList) {

		List<AppointmentResponseDTO> appointmentResponseDTOS = new ArrayList<>();
		for(Appointment a : appointmentsList){
			AppointmentResponseDTO appointmentResponseDTO = new AppointmentResponseDTO();
			appointmentResponseDTO.setId(a.getId());
			appointmentResponseDTO.setAppointmentDate(a.getAppointmentDate());
			appointmentResponseDTO.setAppointmentTime(a.getAppointmentTime());
			if (a.getDoctor() != null) {
				appointmentResponseDTO.setDoctorId(a.getDoctor().getId());
			}
			if (a.getPatient() != null) {
				appointmentResponseDTO.setPatientId(a.getPatient().getId());
			}
			appointmentResponseDTOS.add(appointmentResponseDTO);
		}
		ResponseStructure<List<AppointmentResponseDTO>> rs = new ResponseStructure<>();
		rs.setStatusCode(HttpStatus.OK.value());
		rs.setMessage("Successfully Saved all data");
		rs.setData(appointmentResponseDTOS);
		return new ResponseEntity<ResponseStructure<List<AppointmentResponseDTO>>>(rs,HttpStatus.OK);

	}

}

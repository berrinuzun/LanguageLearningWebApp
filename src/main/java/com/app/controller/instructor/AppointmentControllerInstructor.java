package com.app.controller.instructor;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.entity.Appointment;
import com.app.entity.AvailableSlot;
import com.app.entity.User;
import com.app.repository.AppointmentRepository;
import com.app.service.AppointmentService;
import com.app.service.AvailableSlotService;
import com.app.service.UserService;

@Controller
@RequestMapping("/instructor-page/appointments")
public class AppointmentControllerInstructor {

	@Autowired
	private AvailableSlotService availableSlotService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private AppointmentRepository appointmentRepository;
	
	@GetMapping
	public String showAppointments(Model model) {
		
		List<Appointment> allAppointments = appointmentService.getAllAppointments();
		String currentUser = userService.getCurrentUser().getEmail();
		Long currentUserId = userService.getCurrentUser().getId();
		List<Appointment> appointments = new ArrayList<Appointment>();
		
		for(Appointment appointment : allAppointments) {
			
			if(appointment.getInstructorEmail().equals(currentUser)) {
				
		        appointments.add(appointment);
				
			}
			
		}
		
		List<AvailableSlot> allAvailableAppointments = availableSlotService.getAllSlots();
		List<AvailableSlot> availableAppointments = new ArrayList<AvailableSlot>();
		
		for(AvailableSlot availableAppointment : allAvailableAppointments) {
			
			if(availableAppointment.getInstructor().getId() == currentUserId && availableAppointment.isBooked() == false) {
				
				availableAppointments.add(availableAppointment);
				
			}
			
		}
		
		model.addAttribute("appointments", appointments);
		model.addAttribute("availableAppointments", availableAppointments);
		
		return "myAppointmentsInstructor";
		
	}
	
	@GetMapping("/add-slot")
	public String showAddSlotForm(Model model, Principal principal) {
		
		String email = principal.getName();
		User instructor = userService.findByEmail(email);
		
		if(instructor != null && "instructor".equals(instructor.getRole())) {
			
			model.addAttribute("instructorId", instructor.getId());
			return "addNewAppointment";
			
		}else {
			
			return "redirect:/instructor-page/appointments";
			
		}
		
	}
	
	@PostMapping("/add-slot")
	public String addSlot(@RequestParam Long instructorId,
						  @RequestParam LocalDateTime startTime,
						  @RequestParam LocalDateTime endTime) {
		
		User instructor = userService.findById(instructorId);
		
		if("instructor".equals(instructor.getRole())) {
			
			AvailableSlot slot = new AvailableSlot();
			slot.setInstructor(instructor);
			slot.setStartTime(startTime);
			slot.setEndTime(endTime);
			availableSlotService.saveSlot(slot);
			
			return "redirect:/instructor-page/appointments";
			
		}else {
			
			return "redirect:/instructor-page/appointments";
			
		}
		
	}
	
	
	@PostMapping("/delete/{id}")
	public String cancelAppointment(@PathVariable Long id) {
		
		Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
		
		if(appointmentOpt.isPresent()) {
			
			Appointment appointment = appointmentOpt.get();
			LocalDateTime appointmentTime = appointment.getAppointmentTime();
			Long currentUserId = userService.getCurrentUser().getId();
			
			updateSlotBookedStatus(currentUserId, appointmentTime, false);
			
		}
		
		appointmentService.cancelAppointment(id);
		
		return "redirect:/instructor-page/appointments";
		
	}

	private void updateSlotBookedStatus(Long currentUserId, LocalDateTime appointmentTime, boolean booked) {
		
		List<AvailableSlot> slots = availableSlotService.getAllSlots();
		
		for(AvailableSlot slot : slots) {
			
			if(slot.getInstructor().getId() == currentUserId && slot.getStartTime().isEqual(appointmentTime)) {
				
				slot.setBooked(booked);
				availableSlotService.updateSlotBooking(slot.getId(), booked);
				break;
				
			}
			
		}
		
	}
	
	@PostMapping("/delete-slot/{id}")
	public String cancelSlot(@PathVariable Long id) {
		
		availableSlotService.deleteSlot(id);
		
		return "redirect:/instructor-page/appointments";
		
	}
	
	
}

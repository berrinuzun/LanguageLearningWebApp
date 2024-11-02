package com.app.controller.learner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/learner-page/appointments")
public class AppointmentControllerLearner {

	@Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private UserService userService; 
    
    @Autowired
    private AvailableSlotService availableSlotService;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @GetMapping
    public String getAllAppointments(Model model) {
        
    	List<Appointment> allAppointments = appointmentService.getAllAppointments();
    	String currentUser = userService.getCurrentUser().getEmail();
    	List<Appointment> appointments = new ArrayList<Appointment>();
    	
    	for(Appointment appointment : allAppointments) {
    		
    		if(appointment.getLearnerEmail().equals(currentUser)) {
    			
    			appointments.add(appointment);
    			
    		}
    		
    	}
    	
    	model.addAttribute("appointments", appointments);
        
        return "myAppointmentsLearner";
        
    }
    
    @PostMapping("/delete/{id}")
    public String cancelAppointment(@PathVariable Long id) {
        
    	Optional<Appointment> appointmentOpt = appointmentRepository.findById(id);
    	
    	if(appointmentOpt.isPresent()) {
    		
    		Appointment appointment = appointmentOpt.get();
    		LocalDateTime appointmentTime = appointment.getAppointmentTime();
    		User instructor = userService.findByEmail(appointment.getInstructorEmail());
    		Long instructorId = instructor.getId();
    		
    		updateSlotBookedStatus(instructorId, appointmentTime, false);
    		
    	}
    	
    	appointmentService.cancelAppointment(id);
        return "redirect:/learner-page/appointments";
        
    }


    @GetMapping("/book-appointment")
    public String showCreateForm(Model model) {
        
    	List<User> instructors = userService.getInstructors();
        model.addAttribute("instructors", instructors);
   
        return "selectInstructorFormLearner";
        
    }
    
    @PostMapping("/book-appointment")
    public String showAvailableSlots(@RequestParam Long instructorId, Model model) {
        
    	Optional<User> instructor = userService.getUserById(instructorId);
        
    	if (instructor.isPresent() && instructor.get().getRole().equals("instructor")) {
            
    		List<AvailableSlot> availableSlots = availableSlotService.getAvailableSlotsByInstructorId(instructorId);
            model.addAttribute("instructor", instructor.get());
            model.addAttribute("availableSlots", availableSlots);
            
            return "bookAppointmentPageLearner";
            
        } else {
            
        	return "redirect:/learner-page/appointments/book-appointment";
        
        }
    	
    }
    
    @PostMapping("/confirm")
    public String createAppointment(@RequestParam Long instructorId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime, Model model) {

        Optional<User> instructor = userService.getUserById(instructorId);
        String currentUser = userService.getCurrentUser().getEmail();

        if (instructor.isPresent() && instructor.get().getRole().equals("instructor")) {

            Appointment appointment = new Appointment();
            appointment.setInstructorEmail(instructor.get().getEmail());
            appointment.setLearnerEmail(currentUser);
            appointment.setAppointmentTime(dateTime);

            appointmentService.createAppointment(appointment);
            updateSlotBookedStatus(instructorId, dateTime,true);
            
            return "redirect:/learner-page/appointments";


        } else {
            
        	return "redirect:/learner-page/appointments/book-appointment";
        	
        }
    }

	private void updateSlotBookedStatus(Long instructorId, LocalDateTime dateTime, boolean booked) {
		
		List<AvailableSlot> slots = availableSlotService.getAllSlots();
		
		for(AvailableSlot slot : slots) {
			
			if(slot.getInstructor().getId() == instructorId && slot.getStartTime().isEqual(dateTime)) {
				
				slot.setBooked(booked);
				availableSlotService.updateSlotBooking(slot.getId(), booked);
				break;
				
			}
			
		}
		
	}

    
}

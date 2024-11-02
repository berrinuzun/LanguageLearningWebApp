package com.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.AvailableSlot;
import com.app.repository.AvailableSlotRepository;

@Service
public class AvailableSlotService {

	@Autowired
	private AvailableSlotRepository availableSlotRepository;
	
	public AvailableSlot saveSlot(AvailableSlot slot) {
		
		return availableSlotRepository.save(slot);
		
	}
	
	public List<AvailableSlot> getSlotsByInstructorId(Long instructorId){
		
		return availableSlotRepository.findByInstructorId(instructorId);
		
	}
	
	public void deleteSlot(Long id) {
		
		availableSlotRepository.deleteById(id);
		
	}
	
	public List<AvailableSlot> getAvailableSlotsByInstructorId(Long instructorId){
		
		return availableSlotRepository.findByInstructorId(instructorId);
		
	}
	
	public Optional<AvailableSlot> getAvailableSlotById(Long id) {
        return availableSlotRepository.findById(id);
    }

	public List<AvailableSlot> getAllSlots() {
        return availableSlotRepository.findAll();
    }

	public void updateSlotBooking(Long slotId, boolean booked) {
        AvailableSlot slot = availableSlotRepository.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found with id: " + slotId));

        slot.setBooked(booked);
        availableSlotRepository.save(slot);
    }
	
	
}

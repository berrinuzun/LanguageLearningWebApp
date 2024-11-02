package com.app.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.dto.UserDto;
import com.app.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
        
    	return "registerPage";
    	
    }
    
    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
        
    	if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            
			 model.addAttribute("errorMessage", "Passwords do not match!");
	         return "registerPage";
	         
	     }
	     
		 try {
	     
			 userService.save(userDto);
	         model.addAttribute("message", "Registered Successfully!");
	        
		 } catch (Exception e) {
	     
			 model.addAttribute("errorMessage", "Registration Failed!");
	        
		 }
	        
		 return "registerPage";
        
    }
    
    @GetMapping("/login")
    public String login() {
        
    	return "loginPage";
    	
    }
    
    @GetMapping("learner-page")
    public String learnerPage(Model model, Principal principal) {
        
    	UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "learnerPage";
        
    }
    
    @GetMapping("instructor-page")
    public String instructorPage(Model model, Principal principal) {
        
    	UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "instructorPage";
        
    }
    
    @GetMapping("admin-page")
    public String adminPage(Model model, Principal principal) {
        
    	UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "adminPage";
        
    }
}

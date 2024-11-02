package com.app.controller.admin;

import com.app.entity.User;
import com.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin-page")
public class ProfileControllerAdmin {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/my-profile")
    public String showProfile(Model model) {
        
    	User user = userService.getCurrentUser();
        model.addAttribute("user", user);
        
        return "myProfilePageAdmin";
        
    }

    @PostMapping("/my-profile")
    public String updateProfile(@RequestParam("email") String email,
                                @RequestParam("oldPassword") String oldPassword,
                                @RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                Model model) {

        User user = userService.getCurrentUser();

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            
        	model.addAttribute("updateError", "Old password is incorrect.");
            model.addAttribute("user", user); 
            
            return "myProfilePageAdmin";
            
        }

        if (!newPassword.isEmpty() && !newPassword.equals(confirmPassword)) {
            
        	model.addAttribute("updateError", "Passwords do not match.");
            model.addAttribute("user", user); 
            
            return "myProfilePageAdmin";
            
        }

        if (!newPassword.isEmpty()) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        
        if (!user.getEmail().equals(email)) {
            user.setEmail(email);
        }

        user.setEmail(email);
        userService.save(user);
        model.addAttribute("updateSuccess", "Profile updated successfully!");

        return "redirect:/admin-page/my-profile";
        
    }
}

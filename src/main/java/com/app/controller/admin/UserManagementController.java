package com.app.controller.admin;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.app.entity.User;
import com.app.service.UserService;

@Controller
@RequestMapping("/admin-page/user-management")
public class UserManagementController {

	@Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model) {
        
    	model.addAttribute("users", userService.findAll());
        return "userManagementPage";
        
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        
    	model.addAttribute("user", new User());
        return "addUserForm";
        
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user) {
        
    	userService.save(user);
        return "redirect:/admin-page/user-management";
        
    }

    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        
    	User user = userService.findById(id);
        model.addAttribute("user", user);
        
        return "editUserForm";
        
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
        
    	userService.update(id, user);
        return "redirect:/admin-page/user-management";
        
    }

 
  
	
}

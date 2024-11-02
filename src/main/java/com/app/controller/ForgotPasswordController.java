package com.app.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.ForgotPasswordToken;
import com.app.entity.User;
import com.app.repository.ForgotPasswordRepository;
import com.app.service.ForgotPasswordService;
import com.app.service.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotPasswordController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ForgotPasswordService forgotPasswordService;
	
	@Autowired
	private ForgotPasswordRepository forgotPasswordRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/password-request")
	public String passwordRequest() {
		
		return "passwordRequestPage";
		
	}
	
	@PostMapping("/password-request")
	public String savePasswordRequest(@RequestParam("username") String username, Model model, RedirectAttributes redirectAttributes) {

	    User user = userService.findByEmail(username);

	    if(user == null) {
	        
	    	model.addAttribute("error", "This email is not registered!");
	        return "passwordRequestPage";
	        
	    }

	    ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken();
	    forgotPasswordToken.setExpireTime(forgotPasswordService.expireTimeRange());
	    forgotPasswordToken.setToken(forgotPasswordService.generateToken());
	    forgotPasswordToken.setUser(user);
	    forgotPasswordToken.setUsed(false);

	    forgotPasswordRepository.save(forgotPasswordToken);

	    String emailLink = "http://localhost:8080/reset-password?token=" + forgotPasswordToken.getToken();

	    try {
	        
	    	forgotPasswordService.sendEmail(user.getEmail(), "Password Reset Link", emailLink);
	    	
	    } catch (UnsupportedEncodingException | MessagingException e) {
	        
	    	model.addAttribute("error", "Error while sending email!");
	        return "passwordRequestPage";
	        
	    }

	    redirectAttributes.addAttribute("success", true);
	    return "redirect:/password-request";
	    
	}

	
	@GetMapping("/reset-password")
	public String resetPassword(@Param(value = "token") String token, Model model, HttpSession session) {
		
		session.setAttribute("token", token);
		ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
		
		return forgotPasswordService.checkValidity(forgotPasswordToken, model);
		
	}
	
	@PostMapping("/reset-password")
	public String saveResetPassword(HttpServletRequest request, HttpSession session, Model model) {
		
		String password = request.getParameter("password");
		String token = (String) session.getAttribute("token");
		
		ForgotPasswordToken forgotPasswordToken = forgotPasswordRepository.findByToken(token);
		User user = forgotPasswordToken.getUser();
		user.setPassword(passwordEncoder.encode(password));
		forgotPasswordToken.setUsed(true);
		userService.save(user);
		forgotPasswordRepository.save(forgotPasswordToken);
		
		model.addAttribute("message", "You have successfully reset your password!");
		
		return "resetPasswordPage";
		
	}
	
}

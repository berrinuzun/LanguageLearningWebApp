package com.app.service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.app.entity.ForgotPasswordToken;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class ForgotPasswordService {

	private final int MINUTES = 10;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	public String generateToken() {
		
		return UUID.randomUUID().toString();
		
	}
	
	public LocalDateTime expireTimeRange() {
		
		return LocalDateTime.now().plusMinutes(MINUTES);
		
	}
	
	public void sendEmail(String to, String subject, String emailLink) throws MessagingException, UnsupportedEncodingException{
		
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		String emailContent = "<p>Hello</p>"
								+ "Click the link below to reset password"
								+ "<p> <a href=\"" + emailLink + "\">Change My Password</p>"
								+ "<br>"
								+ "Ignore this email if you did not make the request";
								
		helper.setText(emailContent, true);
		helper.setFrom("", "Language Learning App Support");
		helper.setSubject(subject);
		helper.setTo(to);
		javaMailSender.send(message);
		
	}
	
	public boolean isExpired(ForgotPasswordToken forgotPasswordToken) {
		
		return LocalDateTime.now().isAfter(forgotPasswordToken.getExpireTime());
		
	}
	
	public String checkValidity(ForgotPasswordToken forgotPasswordToken, Model model) {
		
		if(forgotPasswordToken == null) {
			
			model.addAttribute("error", "Invalid Token!");
			return "errorPage";
			
		}else if(forgotPasswordToken.isUsed()) {
			
			model.addAttribute("error", "The token is already used!");
			return "errorPage";
			
		}else if(isExpired(forgotPasswordToken)) {
			
			model.addAttribute("error", "The token is expired!");
			return "errorPage";
			
		}else {
			
			return "resetPasswordPage";
			
		}
		
	}
	
}

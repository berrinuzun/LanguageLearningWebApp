package com.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

	private String fullName;
	private String email;
	private String password;
	private String confirmPassword;
	private String role;
	
}

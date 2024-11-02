package com.app.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String fullName;
	
	@NonNull
	private String email;
	
	@NonNull
	private String password;
	
	private String role;
	
	

	public User(String fullName, @NonNull String email, @NonNull String password, String role) {
		super();
		this.fullName = fullName;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	
	

	
	
	
	
}
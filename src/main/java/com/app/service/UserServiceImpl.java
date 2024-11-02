package com.app.service;

import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dto.UserDto;
import com.app.entity.User;
import com.app.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public User save(UserDto userDto) {
        User user = new User(
                userDto.getFullName(), 
                userDto.getEmail(), 
                passwordEncoder.encode(userDto.getPassword()), 
                userDto.getRole().toLowerCase()
        );
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

	@Override
	public User save(User user) {
		
		return userRepository.save(user);
		
	}

	@Override
	public User getCurrentUser() {
        
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
		if (principal instanceof UserDetails) {
            
			String username = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(username);
            
        } else {
            
        	return null;
        	
        }
    }
    
	@Override
    public List<User> findAll() {
        
		return userRepository.findAll();
		
    }

    @Override
    public User findById(Long id) {
        
    	return userRepository.findById(id).orElse(null);
    	
    }

    @Override
    public void update(Long id, User user) {
        
    	User existingUser = userRepository.findById(id).orElse(null);
        
    	if (existingUser != null) {
            
    		existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            existingUser.setRole(user.getRole());
            userRepository.save(existingUser);
            
        }
    	
    }

    public List<User> getInstructors() {
        
    	return userRepository.findByRole("instructor");
    	
    }

	@Override
	public Optional<User> findByID(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);
	}

	@Override
	public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
	
	
    
}


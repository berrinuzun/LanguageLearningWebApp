package com.app.service;

import java.util.List;
import java.util.Optional;

import com.app.dto.UserDto;
import com.app.entity.User;

public interface UserService {
    
	User save(UserDto userDto);
    User findByEmail(String email);
    User save(User user);
	User getCurrentUser();
	List<User> findAll();
    User findById(Long id);
    void update(Long id, User user);
    List<User> getInstructors();
    Optional<User> findByID(Long id);
    Optional<User> getUserById(Long id);
 
    
}

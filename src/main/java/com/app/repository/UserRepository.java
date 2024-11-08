package com.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	List<User> findByRole(String role);

	

 

	
}

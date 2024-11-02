package com.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
	
}

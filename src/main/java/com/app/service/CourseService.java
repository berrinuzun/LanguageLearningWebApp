package com.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Course;
import com.app.repository.CourseRepository;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;
	
	public List<Course> getAllCourses(){
		return courseRepository.findAll();
	}
	
	public Optional<Course> getCourseById(Long id) {
		return courseRepository.findById(id);
	}
	
	public Course saveCourse(Course course) {
		return courseRepository.save(course);
	}
	
	public void deleteCourse(Long id) {
		courseRepository.deleteById(id);
	}
	
	public void updateCourse(Course course) {
		
		Optional<Course> existingCourseOptional = courseRepository.findById(course.getId());
		
		if(existingCourseOptional.isPresent()) {
			
			Course existingCourse = existingCourseOptional.get();
			existingCourse.setCourseTitle(course.getCourseTitle());
			existingCourse.setContentInformation(course.getContentInformation());
			existingCourse.setCourseImage(course.getCourseImage());
			
		}else {
			
			throw new IllegalArgumentException("Course could not find : " + course.getId());
			
		}
		
	}

	
}
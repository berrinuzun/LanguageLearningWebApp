package com.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Content;
import com.app.repository.ContentRepository;
import com.app.repository.CourseRepository;

@Service
public class ContentService {

	@Autowired
	private ContentRepository contentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	public void saveContent(Content content) {
		contentRepository.save(content);
	}
	
	public Optional<Content> getContentById(Long id){
		return contentRepository.findById(id);
	}
	
	public List<Content> getAllContents(){
		return contentRepository.findAll();
	}
	
	public List<Content> getContentsByCourseId(Long courseId){
		return contentRepository.findByCourseId(courseId);
	}
	
	public void deleteContent(Long id) {
		contentRepository.deleteById(id);
	}
	
	public void updateContent(Content content) {
		contentRepository.save(content);
	}
	
	
}

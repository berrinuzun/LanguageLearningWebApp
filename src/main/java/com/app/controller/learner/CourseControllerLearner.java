package com.app.controller.learner;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Content;
import com.app.entity.Course;
import com.app.entity.File;
import com.app.repository.StorageRepository;
import com.app.service.ContentService;
import com.app.service.CourseService;
import com.app.service.StorageService;

@Controller
@RequestMapping("/learner-page")
public class CourseControllerLearner {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StorageService storageService;
    
    @Autowired
    private StorageRepository storageRepository;
    
    @Autowired
    private ContentService contentService;

    @GetMapping("/courses")
    public String getCourses(Model model) {
        
    	List<Course> courses = courseService.getAllCourses();
        
        courses.forEach(course -> {
            if (course.getCourseImage() != null && course.getCourseImage().getFileData() != null) {
                String base64Image = Base64.getEncoder().encodeToString(course.getCourseImage().getFileData());
                String imageSrc = "data:image/" + course.getCourseImage().getType() + ";base64," + base64Image;
                course.getCourseImage().setName(imageSrc);
            }
        });
        
        model.addAttribute("courses", courses);
        
        return "coursesPageLearner";
    }

    @GetMapping("/courses/{id}")
    public String getCourseById(@PathVariable Long id, Model model) {
        
    	Optional<Course> courseOptional = courseService.getCourseById(id);
        
    	if (courseOptional.isPresent()) {
            
    		Course course = courseOptional.get();
    		Long courseId = course.getId();
    		List<Content> allContents = contentService.getAllContents();
    		List<Content> contents = new ArrayList<Content>();
    		
    		for(Content content : allContents) {
    			if(content.getCourse().getId() == courseId) {
    				contents.add(content);
    			}
    		}

    		model.addAttribute("contents", contents);
    		return "contentPageLearner";
            
        } else {
            return "redirect:/learner-page/courses";
        }
    	
    }

    @GetMapping("/course-image/{fileId}")
    public ResponseEntity<byte[]> getCourseImage(@PathVariable Long fileId) {
        
    	Optional<File> fileOptional = storageRepository.findById(fileId);
        
        if (fileOptional.isPresent()) {
            
        	File file = fileOptional.get();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) 
                    .body(file.getFileData());
            
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

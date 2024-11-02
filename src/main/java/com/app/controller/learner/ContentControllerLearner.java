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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.Content;
import com.app.entity.Course;
import com.app.entity.File;
import com.app.entity.PracticeQuestion;
import com.app.repository.StorageRepository;
import com.app.service.ContentService;
import com.app.service.CourseService;
import com.app.service.PracticeQuestionsService;
import com.app.service.StorageService;

@Controller
@RequestMapping("/learner-page/courses")
public class ContentControllerLearner {

	@Autowired
	private ContentService contentService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private StorageRepository storageRepository;

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private PracticeQuestionsService practiceQuestionsService;

	@GetMapping("/contents/{id}")
	public String getContentById(@PathVariable Long id, Model model) {
		Optional<Content> optionalContent = contentService.getContentById(id);

		if (optionalContent.isPresent()) {
			Content content = optionalContent.get();

			if (content.getContentFile() != null && content.getContentFile().getFileData() != null) {
				String fileDownloadUrl = "/download-file/" + content.getContentFile().getId();
				model.addAttribute("fileDownloadUrl", fileDownloadUrl);
			}

			model.addAttribute("content", content);
			return "contentDetailsPageLearner";
		} else {
			return "redirect:/learner-page/courses/contents/{id}";
		}
	}

	@GetMapping("/download-pdf/{fileId}")
	public ResponseEntity<byte[]> downloadPdfFile(@PathVariable Long fileId) {
	    Optional<File> fileOptional = storageRepository.findById(fileId);

	    if (fileOptional.isPresent()) {
	        File file = fileOptional.get();
	        return ResponseEntity.ok()
	                .contentType(MediaType.APPLICATION_PDF)
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
	                .body(file.getFileData());
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@GetMapping("/content/{id}")
	public String getPracticeQuestionsByContent(@PathVariable Long id, Model model) {
		
		Optional<Content> contentOptional = contentService.getContentById(id);
		Long contentId = null;
		
		if(contentOptional.isPresent()) {
			
			Content content = contentOptional.get();
			contentId = content.getId();
			List<PracticeQuestion> allPracticeQuestions = practiceQuestionsService.getAllPracticeQuestions();
			List<PracticeQuestion> practiceQuestions = new ArrayList<PracticeQuestion>();
			
			for(PracticeQuestion practiceQuestion : allPracticeQuestions) {
				
				if(practiceQuestion.getContent().getId() == contentId) {
					practiceQuestions.add(practiceQuestion);
				}
				
			}
			
			model.addAttribute("practiceQuestions", practiceQuestions);
			return "practiceQuestionsPageLearner";
			
		}else {
			return "redirect:/learner-page/courses/contents/" + contentId;
		}
		
	}

}

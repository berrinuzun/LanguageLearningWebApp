package com.app.controller.instructor;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.entity.Content;
import com.app.entity.Course;
import com.app.entity.PracticeQuestion;
import com.app.service.ContentService;
import com.app.service.PracticeQuestionsService;

@Controller
@RequestMapping("/instructor-page/courses/contents")
public class PracticeQuestionControllerInstructor {
	
	@Autowired
	private PracticeQuestionsService practiceQuestionService;

	@Autowired
	private ContentService contentService;
	
	@GetMapping("/all-practice-questions")
	public String getAllPracticeQuestions(@PathVariable Long id, Model model) {
		
		List<PracticeQuestion> practiceQuestions = practiceQuestionService.getPracticeQuestionsByContentId(id);
		model.addAttribute("practiceQuestions", practiceQuestions);
		
		return "practiceQuestionsPageInstructor";
		
	}
	
	@GetMapping("/create-practice-questions")
	public String createPracticeQuestionsForm(Model model) {
		
		model.addAttribute("practiceQuestions", new PracticeQuestion());
		List<Content> contents = contentService.getAllContents();
		model.addAttribute("contents", contents);
		
		return "createPracticeQuestionsInstructor";
		
	}
	
	@PostMapping("/practice-questions")
	public String practiceQuestions(@RequestParam String question,
									@RequestParam String optionA,
									@RequestParam String optionB,
									@RequestParam String optionC,
									@RequestParam String optionD,
									@RequestParam String correctAnswer,
									@RequestParam Long contentId) {
			
		Optional<Content> contentOptional = contentService.getContentById(contentId);
		
		if(!contentOptional.isPresent()) {
			return "redirect:/instructor-page/courses";
		}
		
		PracticeQuestion practiceQuestion = new PracticeQuestion();
		practiceQuestion.setQuestion(question);
		practiceQuestion.setOptionA(optionA);
		practiceQuestion.setOptionB(optionB);
		practiceQuestion.setOptionC(optionC);
		practiceQuestion.setOptionD(optionD);
		practiceQuestion.setCorrectAnswer(correctAnswer);
		practiceQuestion.setContent(contentOptional.get());
			
		practiceQuestionService.savePracticeQuestion(practiceQuestion);
		
		return "redirect:/instructor-page/courses/contents/" + contentId;
			
	}
	
	@GetMapping("/update-practice-question/{id}")
	public String showUpdatePracticeQuestionForm(@PathVariable Long id, Model model) {
		
		Optional<PracticeQuestion> practiceQuestioOptional = practiceQuestionService.getPracticeQuestionById(id);
		
		if(practiceQuestioOptional.isPresent()) {
			
			List<Content> contents = contentService.getAllContents();
		    model.addAttribute("contents", contents);
			model.addAttribute("practiceQuestions", practiceQuestioOptional.get());
			return "updatePracticeQuestionFormInstructor";
			
		}else {
			
			return "redirect:/instructor-page/courses";
			
		}
		
	}
	
	@PostMapping("/update-practice-question/{id}")
	public String updatePracticeQuestion(@PathVariable Long id,
											@RequestParam Long contentId,
											@RequestParam String question,
											@RequestParam String optionA,
											@RequestParam String optionB,
											@RequestParam String optionC,
											@RequestParam String optionD,
											@RequestParam String correctAnswer) {
		
		Optional<PracticeQuestion> practiceQuestionOptional = practiceQuestionService.getPracticeQuestionById(id);
		Optional<Content> contentOptional = contentService.getContentById(contentId);
		
		if(practiceQuestionOptional.isPresent() && contentOptional.isPresent()) {
			
			PracticeQuestion practiceQuestion = practiceQuestionOptional.get();
			practiceQuestion.setQuestion(question);
			practiceQuestion.setOptionA(optionA);
			practiceQuestion.setOptionB(optionB);
			practiceQuestion.setOptionC(optionC);
			practiceQuestion.setOptionD(optionD);
			practiceQuestion.setCorrectAnswer(correctAnswer);
			practiceQuestion.setContent(contentOptional.get());
			
			practiceQuestionService.savePracticeQuestion(practiceQuestion);
			return "redirect:/instructor-page/courses/contents" + contentId;
			
		}
		return "redirect:/instructor-page/courses";
	}
	
	@PostMapping("/delete-practice-question/{id}")
	public String deletePracticeQuestion(@PathVariable Long id) {
	    Optional<PracticeQuestion> questionOptional = practiceQuestionService.getPracticeQuestionById(id);
	    
	    if (!questionOptional.isPresent()) {  
	        return "redirect:/instructor-page/courses";
	    }

	    Long contentId = questionOptional.get().getContent().getId();
	    practiceQuestionService.deletePracticeQuestion(id);
	    return "redirect:/instructor-page/courses/contents/" + contentId;
	}

	
}

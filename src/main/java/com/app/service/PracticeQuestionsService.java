package com.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.PracticeQuestion;
import com.app.repository.PracticeQuestionsRepository;

@Service
public class PracticeQuestionsService {

	@Autowired
	private PracticeQuestionsRepository practiceQuestionsRepository;
	
	public void savePracticeQuestion(PracticeQuestion practiceQuestion) {
		practiceQuestionsRepository.save(practiceQuestion);
	}
	
	public Optional<PracticeQuestion> getPracticeQuestionById(Long id){
		return practiceQuestionsRepository.findById(id);
	}
	
	public List<PracticeQuestion> getAllPracticeQuestions(){
		return practiceQuestionsRepository.findAll();
	}
	
	public void deletePracticeQuestion(Long id) {
		practiceQuestionsRepository.deleteById(id);
	}
	
	public void updatePracticeQuestion(PracticeQuestion practiceQuestion) {
		practiceQuestionsRepository.save(practiceQuestion);
	}

	public List<PracticeQuestion> getPracticeQuestionsByContentId(Long id) {
		return practiceQuestionsRepository.findByContentId(id);
	}
	
}

package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.PracticeQuestion;

@Repository
public interface PracticeQuestionsRepository extends JpaRepository<PracticeQuestion, Long> {

	List<PracticeQuestion> findByContentId(Long id);

}

package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.Content;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

	List<Content> findByCourseId(Long courseId);
	
}

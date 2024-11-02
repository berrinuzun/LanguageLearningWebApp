package com.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entity.File;

@Repository
public interface StorageRepository extends JpaRepository<File, Long> {

	Optional<File> findByName(String fileName);
	
}

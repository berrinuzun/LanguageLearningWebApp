package com.app.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor	@AllArgsConstructor
public class Content {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "course_id")
	private Course course;
	
	private String contentTitle;
	private String contentInformation;
	
	@OneToOne(cascade = CascadeType.ALL) 
	@JoinColumn(name = "file_id", referencedColumnName = "id")
	private File contentFile;
	

	
}

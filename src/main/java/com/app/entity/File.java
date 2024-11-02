package com.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor	@NoArgsConstructor
@Builder
public class File {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String type;
	
	@Lob
	@Column(name = "fileData", columnDefinition = "LONGBLOB")
	private byte[] fileData;

	public File(String name, String type, byte[] fileData) {
		super();
		this.name = name;
		this.type = type;
		this.fileData = fileData;
	}


	
	
	
}

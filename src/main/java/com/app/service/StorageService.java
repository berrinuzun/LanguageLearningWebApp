package com.app.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.entity.File;
import com.app.repository.StorageRepository;
import com.app.utils.FileUtils;

@Service
public class StorageService {

	@Autowired
	private StorageRepository storageRepository;
	
	public File uploadFile(MultipartFile file) throws IOException, IllegalAccessException {
		
		File f = storageRepository.save(
											File.builder()
															.name(file.getOriginalFilename())
															.type(file.getContentType())
															.fileData(FileUtils.compressFile(file.getBytes())).build()
				);
		
		if(f == null) {
			throw new IllegalArgumentException("File " + file.getOriginalFilename() + " could not upload !");
		}
		
		return f;
		
	}
	
	public byte[] downloadFile(String fileName) {
		
		Optional<File> dbFileData = storageRepository.findByName(fileName);
		byte[] files = FileUtils.decompressFile(dbFileData.get().getFileData());
		
		return files;
		
	}
	
	public void deleteFile(Long id) {
		storageRepository.deleteById(id);
	}
	
}

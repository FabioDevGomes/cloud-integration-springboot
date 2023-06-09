package com.fabio.controller;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fabio.service.AzureFileService;

@Controller
public class FileAzureController {

	@Autowired
	private AzureFileService fileService;
	
	@GetMapping("/fileAzure")
	public String viewHomePage() {
		return "azureUpload";
	}
	
	@PostMapping(value = "/fileAzure/upload", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
	public ResponseEntity<?> uploadAndDownload(Model model, @RequestParam("file") MultipartFile file){
		try {
			if(fileService.uploadAndDowloadFile(file)) {
				final ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(
						Paths.get(fileService.getFileStorageLocation() + "/" + file.getOriginalFilename())));
				return ResponseEntity.status(HttpStatus.OK).contentLength(resource.contentLength()).body(resource);
			}
			return ResponseEntity.ok("Error while processing file");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.valueOf(500)).build();
		}
		
	}
}

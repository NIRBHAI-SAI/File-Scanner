package com.fscan.File.Scanner.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileControllerService {

    ResponseEntity<String> FileHandlerService(MultipartFile file);
    ResponseEntity<String> AIdHandlerService(String id);
}

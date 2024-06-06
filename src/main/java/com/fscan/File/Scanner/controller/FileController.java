package com.fscan.File.Scanner.controller;


import com.fscan.File.Scanner.service.FileControllerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileController {


    @Autowired
    private FileControllerService fileControllerService;

    @PostMapping("/upload")
    public ResponseEntity<String> fileHandler(@RequestParam("file") MultipartFile file){

        return fileControllerService.FileHandlerService(file);

    }

    @PostMapping("/ScanById")
    public ResponseEntity<String> IdHandler(@RequestParam("id") String id){

        return fileControllerService.AIdHandlerService(id);

    }

}

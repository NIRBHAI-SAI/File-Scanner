package com.fscan.File.Scanner.controller;


import com.fscan.File.Scanner.APIconnector.VirusTotal;
import com.fscan.File.Scanner.utils.Validators;
import com.fscan.File.Scanner.utils.evalJSON;
import com.fscan.File.Scanner.utils.sha256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;


@RestController
@RequestMapping("/api")
public class FileController {

    /*
        scanResponse =
        {
            "malicious": 0,
            "type-unsupported": 15,
            "failure": 0,
            "undetected": 58,
            "suspicious": 0,
            "confirmed-timeout": 0,
            "harmless": 0,
            "timeout": 0
         }
    */

    @PostMapping("/upload")
    public ResponseEntity<String> fileHandler(@RequestParam("file") MultipartFile file){

        String scanResponse = VirusTotal.ScanByFile(file);
        return new ResponseEntity<>(scanResponse,HttpStatus.ACCEPTED);


    }
    @PostMapping("/ScanById")
    public ResponseEntity<String> IdHandler(@RequestParam("id") String id){

        String scanResponse = VirusTotal.ScanById(id);
        return new ResponseEntity<>(scanResponse,HttpStatus.ACCEPTED);
    }

}

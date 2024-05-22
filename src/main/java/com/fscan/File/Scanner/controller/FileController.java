package com.fscan.File.Scanner.controller;


import com.fscan.File.Scanner.APIconnector.VirusTotal;
import com.fscan.File.Scanner.evaluator.evalJSON;
import com.fscan.File.Scanner.utils.sha256;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/upload")
    public ResponseEntity<String> fileHandler(@RequestParam("file") MultipartFile file){

        String hexCode = sha256.generate(file);

        if(hexCode.equals("00")){//unable to generate SHA256 for given file.
            return new ResponseEntity<>("IOException", HttpStatus.NOT_ACCEPTABLE);
        }
        String result = evalJSON.analysisStats(VirusTotal.ScanByHex(hexCode));
        if(Objects.equals(result, "NotFoundError")){
            String analysis_id = null;
            analysis_id = VirusTotal.UploadFile(file);
            return new ResponseEntity<>("File uploaded to VT DB analysis id:"+analysis_id,HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(result,HttpStatus.ACCEPTED);

    }

}

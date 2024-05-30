package com.fscan.File.Scanner.controller;


import com.fscan.File.Scanner.APIconnector.VirusTotal;
import com.fscan.File.Scanner.evaluator.evalJSON;
import com.fscan.File.Scanner.utils.sha256;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;


@RestController
@RequestMapping("/api")
public class FileController {



    @PostMapping("/upload")
    public ResponseEntity<String> fileHandler(@RequestParam("file") MultipartFile file){

        String originalFileName = file.getOriginalFilename();
        String name = file.getName();
        String contentType = file.getContentType();
        byte[] content = new byte[0];
        try {
            content = file.getBytes();
        } catch (IOException e) {
            return new ResponseEntity<>("Unable to read multipart file",HttpStatus.NOT_ACCEPTABLE);
        }

        String hexCode = sha256.generate(file);
        String result = "NotFoundError";
        if(hexCode.equals("00")){//unable to generate SHA256 for given file.
            return new ResponseEntity<>("unable to generate SHA256 for given file.", HttpStatus.NOT_ACCEPTABLE);
        }

        result = evalJSON.analysisStats(VirusTotal.ScanByHex(hexCode));//get results by using HEXcode
        //result will be either a proper stats(malicious,harmless,undetected) or Not foundError (if hex code is not present in DB).

        if(Objects.equals(result, "NotFoundError")){//File is not found in Virus Total DataBase

            MultipartFile mockMultipartFile = new MockMultipartFile(name,originalFileName, contentType, content);
            // initial file will be available for GC as one request is made(Scan by hexcode) so creating a multipart file.

            String analysis_id = VirusTotal.UploadFile(mockMultipartFile);//Getting Analysis ID from VT
            if(analysis_id.charAt(analysis_id.length()-1) == '=' && analysis_id.charAt(analysis_id.length()-2)== '='){
                //we received a proper analysis_id
                return new ResponseEntity<>("File uploaded to Virus Total DataBase" +
                        " with analysis ID: "+analysis_id,HttpStatus.ACCEPTED);

            }
            return new ResponseEntity<>(analysis_id, HttpStatus.BAD_GATEWAY);//any error while
            //uploading the file.

        }

        return new ResponseEntity<>(result,HttpStatus.ACCEPTED);//if found in Database.

    }
    @PostMapping("/ScanById")
    public ResponseEntity<String> IdHandler(@RequestParam("id") String id){
        String[] scanResponse = VirusTotal.ScanById(id);
        if(scanResponse.length == 2){
            return new ResponseEntity<>(scanResponse[0],HttpStatus.ACCEPTED);
        }
        if(Objects.equals(scanResponse[0], "queued")){
            return new ResponseEntity<>("Staus of the file is Queued,please try again after some time",HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("error :"+scanResponse[0],HttpStatus.NOT_ACCEPTABLE);
    }

}

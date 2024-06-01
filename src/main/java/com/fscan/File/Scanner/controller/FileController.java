package com.fscan.File.Scanner.controller;


import com.fscan.File.Scanner.APIconnector.VirusTotal;
import com.fscan.File.Scanner.FileAuditDTO.FileAuditDTO;
import com.fscan.File.Scanner.entity.FileAudit;
import com.fscan.File.Scanner.service.FileAuditService;
import com.fscan.File.Scanner.utils.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private  FileAuditService fileAuditService;

    @PostMapping("/upload")
    public ResponseEntity<String> fileHandler(@RequestParam("file") MultipartFile file){

        FileAuditDTO fileAuditDTO = fileAuditService.save(file.getOriginalFilename(), "File uploaded to DB");
        Long id  = fileAuditDTO.getId();


        String scanResponse = VirusTotal.ScanByFile(file,fileAuditService,id);
        if(Validators.isValidResult(scanResponse)){
            String scanResults = Validators.FinalizeVerdict(scanResponse);
            fileAuditService.updateScanResults(id,scanResults);
            fileAuditService.updateDT(id);

            return new ResponseEntity<>(scanResults,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(scanResponse,HttpStatus.ACCEPTED);


    }
    @PostMapping("/ScanById")
    public ResponseEntity<String> IdHandler(@RequestParam("id") String id){


        FileAudit fileAuditDTO = fileAuditService.findByAnalysisId(id);
        Long uid  = fileAuditDTO.getId();

        String scanResponse = VirusTotal.ScanById(id,fileAuditService,uid);
        if(Validators.isValidResult(scanResponse)){
            String scanResults = Validators.FinalizeVerdict(scanResponse);

            fileAuditService.updateScanResults(uid,scanResults);
            fileAuditService.updateDT(uid);

            return new ResponseEntity<>(scanResults,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(scanResponse,HttpStatus.ACCEPTED);
    }

}

package com.fscan.File.Scanner.serviceImplementation;

import com.fscan.File.Scanner.APIconnector.VirusTotal;
import com.fscan.File.Scanner.FileAuditDTO.FileAuditDTO;
import com.fscan.File.Scanner.entity.FileAudit;
import com.fscan.File.Scanner.service.FileAuditService;
import com.fscan.File.Scanner.service.FileControllerService;
import com.fscan.File.Scanner.utils.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileControllerServiceImpl implements FileControllerService {


    @Autowired
    private FileAuditService fileAuditService;

    @Autowired
    private VirusTotal virusTotal;


    public ResponseEntity<String> FileHandlerService(MultipartFile file){
        FileAuditDTO fileAuditDTO = fileAuditService.save(file.getOriginalFilename(), "File uploaded to DB");
        Long id  = fileAuditDTO.getId();


        String scanResponse = virusTotal.ScanByFile(file,fileAuditService,id);

        if(Validators.isValidResult(scanResponse)){
            String scanResults = Validators.FinalizeVerdict(scanResponse);
            fileAuditService.updateScanResults(id,scanResults);
            fileAuditService.updateDT(id);

            return new ResponseEntity<>(scanResults, HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>(scanResponse,HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<String> AIdHandlerService(String id) {

        FileAudit fileAuditDTO = fileAuditService.findByAnalysisId(id);
        Long uid = null;
        try{
            uid  = fileAuditDTO.getId();
        }
        catch(NullPointerException ex){
            return new ResponseEntity<>("Analysis Id is not present in database. " +
                    "\n Enter a proper analysis Id",HttpStatus.BAD_REQUEST);

        }

        String scanResponse = virusTotal.ScanById(id,fileAuditService,uid);
        if(Validators.isValidResult(scanResponse)){
            String scanResults = Validators.FinalizeVerdict(scanResponse);

            fileAuditService.updateScanResults(uid,scanResults);
            fileAuditService.updateDT(uid);

            return new ResponseEntity<>(scanResults,HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(scanResponse,HttpStatus.ACCEPTED);

    }
}

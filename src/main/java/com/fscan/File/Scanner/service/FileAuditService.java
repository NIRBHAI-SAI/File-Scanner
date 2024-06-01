package com.fscan.File.Scanner.service;


import com.fscan.File.Scanner.FileAuditDTO.FileAuditDTO;
import com.fscan.File.Scanner.entity.FileAudit;
import org.springframework.stereotype.Service;

@Service
public interface FileAuditService {

     FileAuditDTO save(String fileName,String status);

     void updateStatus(Long id,String Status);

     void updateSHA256(Long id,String hexcode);

     void updateAID(Long id,String AID);

     void updateDT(Long id);

     void updateScanResults(Long id,String results);

     FileAudit findByAnalysisId(String id);

}

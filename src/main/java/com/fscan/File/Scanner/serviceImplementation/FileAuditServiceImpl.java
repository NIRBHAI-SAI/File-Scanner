package com.fscan.File.Scanner.serviceImplementation;


import com.fscan.File.Scanner.FileAuditDTO.FileAuditDTO;
import com.fscan.File.Scanner.entity.FileAudit;
import com.fscan.File.Scanner.repository.FileAuditRepo;
import com.fscan.File.Scanner.service.FileAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FileAuditServiceImpl implements FileAuditService {


    @Autowired
    private FileAuditRepo fileAuditRepo;



    @Override
    public FileAuditDTO save(String fileName,String status) {

        FileAudit fileAudit = DtoToEntity(fileName,status);

        FileAudit savedAudit = fileAuditRepo.save(fileAudit);

        return EntityToDTO(savedAudit);

    }

    @Override
    public void updateStatus(Long id,String status) {
        FileAudit savedAudit = fileAuditRepo.findById(id).get();
        savedAudit.setStatus(status);
        fileAuditRepo.save(savedAudit);

    }

    @Override
    public void updateSHA256(Long id, String hexcode) {
        FileAudit savedAudit = fileAuditRepo.findById(id).get();
        savedAudit.setSHA256(hexcode);
        fileAuditRepo.save(savedAudit);

    }

    @Override
    public void updateAID(Long id, String AID) {
        FileAudit savedAudit = fileAuditRepo.findById(id).get();
        savedAudit.setAnalysisId(AID);
        fileAuditRepo.save(savedAudit);

    }

    @Override
    public void updateDT(Long id) {
        FileAudit savedAudit = fileAuditRepo.findById(id).get();
        savedAudit.setLast_status_time(LocalDateTime.now());
        fileAuditRepo.save(savedAudit);

    }

    @Override
    public void updateScanResults(Long id, String results) {
        FileAudit savedAudit = fileAuditRepo.findById(id).get();
        savedAudit.setScanResults(results);
        fileAuditRepo.save(savedAudit);
    }

    @Override
    public FileAudit findByAnalysisId(String AId) {
        FileAudit savedAudit = fileAuditRepo.findByanalysisId(AId);
        return savedAudit;
    }


    private FileAudit DtoToEntity(String fileName,String status){
        FileAudit fileAudit = new FileAudit();

        fileAudit.setFileName(fileName);
        fileAudit.setStatus(status);
        fileAudit.setAnalysisId("");
        fileAudit.setScanResults("");
        fileAudit.setSHA256("");
        fileAudit.setLast_status_time(LocalDateTime.now());

        return fileAudit;
    }

    private FileAuditDTO EntityToDTO(FileAudit fileAudit){
        FileAuditDTO fileAuditDTO = new FileAuditDTO();

        fileAuditDTO.setId(fileAudit.getId());
        fileAuditDTO.setFileName(fileAudit.getFileName());
        fileAuditDTO.setSHA256(fileAudit.getSHA256());
        fileAuditDTO.setStatus(fileAudit.getStatus());
        fileAuditDTO.setScanResults(fileAudit.getScanResults());
        fileAuditDTO.setAnalysisId(fileAudit.getAnalysisId());
        fileAuditDTO.setLast_updated_status(fileAudit.getLast_status_time());

        return fileAuditDTO;

    }
}

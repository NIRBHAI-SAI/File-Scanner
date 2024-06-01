package com.fscan.File.Scanner.FileAuditDTO;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FileAuditDTO {

    private Long id;
    private String fileName;
    private String status;
    private String SHA256;
    private String analysisId;
    private String scanResults;
    private LocalDateTime last_updated_status;

    public LocalDateTime getLast_updated_status() {
        return last_updated_status;
    }

    public String getScanResults() {
        return scanResults;
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public String getSHA256() {
        return SHA256;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSHA256(String SHA256) {
        this.SHA256 = SHA256;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public void setScanResults(String scanResults) {
        this.scanResults = scanResults;
    }

    public void setLast_updated_status(LocalDateTime last_updated_status) {
        this.last_updated_status = last_updated_status;
    }
}

package com.fscan.File.Scanner.entity;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name = "file_logs",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"analysisId","SHA256"})
})
public class FileAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "FileName",nullable = false)
    private String fileName;

    @Column(name = "AID",nullable = true)
    private String analysisId;

    @Column(name = "Status",nullable = false)
    private String status;

    @Column(name = "ScanResults",nullable = true)
    private String scanResults;

    @Column(name = "LastStatusTime",nullable = false)
    private LocalDateTime last_status_time;

    @Column(name = "SHA256",nullable = true)
    private String SHA256;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setAnalysisId(String analysisId) {
        this.analysisId = analysisId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setScanResults(String scanResults) {
        this.scanResults = scanResults;
    }

    public void setLast_status_time(LocalDateTime last_status_time) {
        this.last_status_time = last_status_time;
    }

    public void setSHA256(String SHA256) {
        this.SHA256 = SHA256;
    }

    public String getFileName() {
        return fileName;
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public String getStatus() {
        return status;
    }

    public String getScanResults() {
        return scanResults;
    }

    public LocalDateTime getLast_status_time() {
        return last_status_time;
    }

    public String getSHA256() {
        return SHA256;
    }
}

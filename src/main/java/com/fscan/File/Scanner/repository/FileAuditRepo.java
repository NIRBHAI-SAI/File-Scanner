package com.fscan.File.Scanner.repository;

import com.fscan.File.Scanner.entity.FileAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface FileAuditRepo extends JpaRepository<FileAudit,Long> {
    FileAudit findByanalysisId(String analysisId);
}

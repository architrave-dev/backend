package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.global.aop.Trace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Trace
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}

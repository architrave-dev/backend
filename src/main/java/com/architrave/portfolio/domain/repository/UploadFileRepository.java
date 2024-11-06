package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.jpa.repository.JpaRepository;


@Trace
public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}

package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Document;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.jpa.repository.JpaRepository;

@Trace
public interface DocumentRepository extends JpaRepository<Document, Long> {
}

package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.TextBox;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Trace
@Repository
public interface TextBoxRepository extends JpaRepository<TextBox, Long> {
}

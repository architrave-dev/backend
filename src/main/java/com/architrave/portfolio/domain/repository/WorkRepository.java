package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.global.aop.Trace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Trace
public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findByMember(Member member);
}

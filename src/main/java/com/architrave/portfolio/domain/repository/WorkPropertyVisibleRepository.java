package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.WorkPropertyVisible;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


@Trace
public interface WorkPropertyVisibleRepository extends JpaRepository<WorkPropertyVisible, Long> {
    Optional<WorkPropertyVisible> findByMember(Member member);
}

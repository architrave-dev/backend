package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.api.dto.work.response.WorkSimpleDto;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Trace
public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findByMember(Member member);

    @Query("Select new com.architrave.portfolio.api.dto.work.response.WorkSimpleDto(w.id, w.title, w.uploadFile.originalUrl) " +
            "FROM Work w WHERE w.member = :member")
    List<WorkSimpleDto> findSimpleByMember(@Param("member") Member member);
}

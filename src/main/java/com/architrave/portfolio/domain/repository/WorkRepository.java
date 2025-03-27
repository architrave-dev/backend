package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.api.dto.work.response.WorkSimpleDto;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Trace
public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Work> findByMember(Member member);

    // Updated to return a Page<Work> with pagination support
    Page<Work> findByMember(Member member, Pageable pageable);

    @Query("Select new com.architrave.portfolio.api.dto.work.response.WorkSimpleDto(w.id, w.title, w.uploadFile.originUrl) " +
            "FROM Work w WHERE w.member = :member")
    List<WorkSimpleDto> findSimpleByMember(@Param("member") Member member);

    @Modifying
    @Query("""
            DELETE FROM Work w
            WHERE w.member = :member
            """)
    void deleteByMember(@Param("member") Member member);
}

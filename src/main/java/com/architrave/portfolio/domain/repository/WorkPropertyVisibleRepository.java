package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.WorkPropertyVisible;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


@Trace
public interface WorkPropertyVisibleRepository extends JpaRepository<WorkPropertyVisible, Long> {
    Optional<WorkPropertyVisible> findByMember(Member member);

    @Modifying
    @Query("""
            DELETE FROM WorkPropertyVisible wpv
            WHERE wpv.member = :member
            """)
    void deleteByMember(@Param("member") Member member);
}

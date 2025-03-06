package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Trace
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByMember(Member member);

    @Query("select p from Project p" +
            " where p.member = :member" +
            " and p.id = :projectId")
    Optional<Project> findByMemberAndTitle(
            @Param("member") Member member,
            @Param("projectId") Long projectId);


    @Modifying
    @Query("""
            DELETE FROM Project p
            WHERE p.member = :member
            """)
    void deleteByMember(@Param("member") Member member);
}

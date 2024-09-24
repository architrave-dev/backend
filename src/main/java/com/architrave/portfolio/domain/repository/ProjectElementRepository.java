package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.global.aop.Trace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Trace
@Repository
public interface ProjectElementRepository extends JpaRepository<ProjectElement, Long> {
    @Query("select pe from ProjectElement pe" +
            " where pe.project = :project")
    List<ProjectElement> findByProject(
            @Param("project") Project project);

    @Modifying
    @Query("""
            DELETE FROM ProjectElement pe
            WHERE pe.project.member = :member
            AND pe.work = :work
            """)
    void deleteByProjectMemberAndWork(
            @Param("member")Member member,
            @Param("work")Work work);
}

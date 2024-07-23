package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectElementRepository extends JpaRepository<ProjectElement, Long> {
    @Query("select pe from ProjectElement pe" +
            " where pe.project = :project" +
            " order by pe.peOrder")
    List<ProjectElement> findByProject(
            @Param("project") Project project);
}

package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectInfoRepository extends JpaRepository<ProjectInfo, Long> {
    List<ProjectInfo> findByProject(Project project);
    void deleteByProject(Project project);
}

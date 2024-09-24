package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
import com.architrave.portfolio.domain.repository.ProjectInfoRepository;
import com.architrave.portfolio.global.aop.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Trace
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectInfoService {

    private final ProjectInfoRepository projectInfoRepository;

    @Transactional(readOnly = true)
    public List<ProjectInfo> findProjectInfoByProject(Project project){
        return projectInfoRepository.findByProject(project);
    }

    @Transactional(readOnly = true)
    public ProjectInfo findProjectInfoById(Long projectInfoId){
        return projectInfoRepository.findById(projectInfoId)
                .orElseThrow(() -> new NoSuchElementException("no"));
    }

    @Transactional
    public ProjectInfo createProjectInfo(Project project, String name, String value) {
        ProjectInfo projectInfo = ProjectInfo.createProjectInfo(project, name, value);
        return projectInfoRepository.save(projectInfo);
    }


    @Transactional
    public ProjectInfo updateProjectInfo(Long projectInfoId, String name, String value){
        ProjectInfo projectInfo = findProjectInfoById(projectInfoId);
        projectInfo.setCustomName(name);
        projectInfo.setCustomValue(value);

        return projectInfo;
    }

    @Transactional
    public void removeProjectInfoById(Long projectInfoId){
        ProjectInfo projectInfo = findProjectInfoById(projectInfoId);
        projectInfoRepository.delete(projectInfo);
    }

    @Transactional
    public void removeProjectInfoByProject(Project project){
        projectInfoRepository.deleteByProject(project);
    }
}

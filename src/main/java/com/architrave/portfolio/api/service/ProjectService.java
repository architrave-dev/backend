package com.architrave.portfolio.api.service;

import com.architrave.portfolio.api.dto.reorder.request.ReorderReq;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.domain.repository.ProjectRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Trace
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final UploadFileService uploadFileService;

    @Transactional
    public Project createProject(
            Member loginUser,
            String originUrl,
            String title,
            String description
    ) {
        Project project = new ProjectBuilder()
                .member(loginUser)
                .originUrl(originUrl)
                .title(title)
                .description(description)
                .build();

        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public Project findById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no project that id" + id));
    }

    @Transactional(readOnly = true)
    public List<Project> findByMember(Member member) {
        return projectRepository.findByMember(member);
    }

    @Transactional(readOnly = true)
    public Project findByMemberAndProjectId(Member member, Long projectId) {
        return projectRepository.findByMemberAndTitle(member, projectId)
                .orElseThrow(() -> new NoSuchElementException("there is no project that title"));
    }

    @Transactional
    public Project updateProject(
            Long projectId,
            String originUrl,
            String title,
            String description
    ) {
        Project project = findById(projectId);
        if(!project.getTitle().equals(title)) project.setTitle(title);
        if(!project.getDescription().equals(description)) project.setDescription(description);
        if(!project.getUploadFile().getOriginUrl().equals(originUrl)
        ){
            uploadFileService.deleteUploadFile(project.getUploadFile());
            project.setUploadFileUrl(originUrl);
        }

        return project;
    }
    @Transactional
    public void removeProject(Project project){
        uploadFileService.deleteUploadFile(project.getUploadFile());
        projectRepository.delete(project);
    }
    @Transactional
    public void removeProject(Long projectId){
        Project projectById = findById(projectId);
        projectRepository.delete(projectById);
    }
    @Transactional
    public void removeByMember(Member member) {
        projectRepository.deleteByMember(member);
    }

    @Transactional
    public List<Project> reorder(Member member, List<ReorderReq> reorderReqList) {
        Map<Long, Integer> reorderMap = reorderReqList.stream()
                .collect(Collectors.toMap(ReorderReq::getId, ReorderReq::getIndex));

        List<Project> projectList = projectRepository.findByMember(member);

        for (Project project : projectList) {
            Integer newIndex = reorderMap.get(project.getId());
            if(newIndex != null){
                if (project.getIndex() == null || !newIndex.equals(project.getIndex())) {
                    project.setIndex(newIndex);
                }
            }
        }
        return projectRepository.saveAll(projectList);
    }
}

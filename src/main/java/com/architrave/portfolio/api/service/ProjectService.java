package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.domain.repository.ProjectRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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
            String thumbnailUrl,
            String title,
            String description
    ) {
        Project project = new ProjectBuilder()
                .member(loginUser)
                .originUrl(originUrl)
                .thumbnailUrl(thumbnailUrl)
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
  
    @Transactional(readOnly = true)
    public Project findByMemberAndTitleWithElement(Member member, String title) {
        return projectRepository.findByMemberAndTitleWithElement(member, title)
                .orElseThrow(() -> new NoSuchElementException("there is no project that title"));
    }

    @Transactional
    public Project updateProject(
            Long projectId,
            String originUrl,
            String thumbnailUrl,
            String title,
            String description
    ) {
        Project project = findById(projectId);
        if(!project.getTitle().equals(title)) project.setTitle(title);
        if(!project.getDescription().equals(description)) project.setDescription(description);
        if(!project.getUploadFile().getOriginUrl().equals(originUrl) ||
           !project.getUploadFile().getThumbnailUrl().equals(thumbnailUrl)
        ){
            uploadFileService.deleteUploadFile(project.getUploadFile());
            project.setUploadFileUrl(originUrl, thumbnailUrl);
        }

        return project;
    }
    @Transactional
    public void removeProject(Project project){
        projectRepository.delete(project);
    }
    @Transactional
    public void removeProject(Long projectId){
        Project projectById = findById(projectId);
        projectRepository.delete(projectById);
    }

    @Transactional
    public Project updatePiIndex(Long projectId, String piIndex) {
        Project project = findById(projectId);
        project.setPiIndex(piIndex);
        return project;
    }
    @Transactional
    public Project updatePeIndex(Long projectId, String peIndex) {
        Project project = findById(projectId);
        project.setPeIndex(peIndex);
        return project;
    }
}

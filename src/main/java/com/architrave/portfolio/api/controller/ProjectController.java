package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.project.request.CreateProjectReq;
import com.architrave.portfolio.api.dto.project.request.ProjectInfoReq;
import com.architrave.portfolio.api.dto.project.request.UpdateProjectReq;
import com.architrave.portfolio.api.dto.project.response.ProjectDto;
import com.architrave.portfolio.api.dto.project.response.ProjectInfoDto;
import com.architrave.portfolio.api.dto.project.response.ProjectSimpleDto;
import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.api.service.ProjectInfoService;
import com.architrave.portfolio.api.service.ProjectService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;
    private final AuthService authService;
    private final MemberService memberService;
    private final ProjectInfoService projectInfoService;

    @GetMapping("/list")
    public ResponseEntity<ResultDto<List<ProjectSimpleDto>>> getProjectList(
            @RequestParam("aui") String aui
    ){
        log.info("hello from getProjectList");

        Member member = memberService.findMemberByAui(aui);
        List<Project> projectList = projectService.findByMember(member);
        List<ProjectSimpleDto> result = projectList.stream()
                .map((p) -> new ProjectSimpleDto(p))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(result));
    }

    @GetMapping("/{title}")
    public ResponseEntity<ResultDto<ProjectDto>> getProjectDetail(
            @PathVariable("title") String title,
            @RequestParam("aui") String aui
    ){
        log.info("hello from getProjectDetail");
        Member member = memberService.findMemberByAui(aui);
        Project project = projectService.findByMemberAndTitle(member, title);
        List<ProjectInfo> projectInfoList = projectInfoService.findProjectInfoByProject(project);

        List<ProjectInfoDto> projectInfoDtoList = projectInfoList.stream()
                .map((pi) -> new ProjectInfoDto(pi))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectDto(project, projectInfoDtoList)));
    }

    @PostMapping
    public ResponseEntity<ResultDto<ProjectSimpleDto>> createProject(
            @RequestParam("aui") String aui,
            @RequestBody CreateProjectReq createProjectReq
    ){
        log.info("hello from createProject");
        Member loginUser = authService.getMemberFromContext();
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        Project project = new ProjectBuilder()
                .member(loginUser)
                .title(createProjectReq.getTitle())
                .description(createProjectReq.getDescription())
                .build();
        Project createdProject = projectService.createProject(project);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectSimpleDto(createdProject)));
    }

    /**
     *
     * @param aui
     * @param updateProjectReq
     * @return
     */
    @PutMapping
    public ResponseEntity<ResultDto<ProjectDto>> updateProject(
            @RequestParam("aui") String aui,
            @RequestBody UpdateProjectReq updateProjectReq
    ){
        log.info("hello from updateProject");
        Member loginUser = authService.getMemberFromContext();
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        //대상 project 가져오기
        Project targetProject = projectService.findById(updateProjectReq.getId());

        updateProjectInfo(targetProject,
                updateProjectReq.getCreatedProjectInfoList(),
                updateProjectReq.getUpdatedProjectInfoList(),
                updateProjectReq.getRemovedProjectInfoList());

        List<ProjectInfo> projectInfoList = projectInfoService.findProjectInfoByProject(targetProject);
        List<ProjectInfoDto> projectInfoDtoList = projectInfoList.stream()
                .map((p) -> new ProjectInfoDto(p))
                .collect(Collectors.toList());

        Project updateProject = projectService.updateProject(
                targetProject,
                updateProjectReq.getTitle(),
                updateProjectReq.getDescription(),
                updateProjectReq.getStartDate(),
                updateProjectReq.getEndDate(),
                updateProjectReq.getSupportedBy(),
                updateProjectReq.getIsDeleted()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectDto(updateProject, projectInfoDtoList)));
    }

    private void updateProjectInfo(Project targetProject,
                       List<ProjectInfoReq> createdList,
                       List<ProjectInfoReq> updatedList,
                       List<Long> removedList
                       ){
        createdList.stream()
                .forEach((p) -> projectInfoService.createProjectInfo(
                        targetProject,
                        p.getCustomName(),
                        p.getCustomValue()));

        updatedList.stream()
                .forEach((p) -> projectInfoService.updateProjectInfo(
                        p.getId(),
                        p.getCustomName(),
                        p.getCustomValue()));

        removedList.stream()
                .forEach((id) -> projectInfoService.removeProjectInfo(id));
    }
}

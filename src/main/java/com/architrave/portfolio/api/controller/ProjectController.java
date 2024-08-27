package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.project.request.*;
import com.architrave.portfolio.api.dto.project.response.ProjectDto;
import com.architrave.portfolio.api.dto.project.response.ProjectInfoDto;
import com.architrave.portfolio.api.dto.project.response.ProjectSimpleDto;
import com.architrave.portfolio.api.dto.projectElement.response.ProjectElementDto;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "3. Project")  // => swagger 이름
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;
    private final AuthService authService;
    private final MemberService memberService;
    private final ProjectInfoService projectInfoService;

    @Operation(summary = "작가의 Project List 조회하기")
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

    @Operation(summary = "작가의 Project 세부내용 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<ProjectDto>> getProjectDetail(
            @RequestParam("aui") String aui,
            @RequestParam("title") String title
    ){
        log.info("hello from getProjectDetail");
        Member member = memberService.findMemberByAui(aui);
        Project project = projectService.findByMemberAndTitleWithElement(member, title);
        List<ProjectInfo> projectInfoList = projectInfoService.findProjectInfoByProject(project);

        List<ProjectInfoDto> projectInfoDtoList = projectInfoList.stream()
                .map((pi) -> new ProjectInfoDto(pi))
                .collect(Collectors.toList());

        List<ProjectElementDto> projectElementDtoList = project.getProjectElementList()
                .stream()
                .map((pe) -> new ProjectElementDto(pe))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectDto(project, projectInfoDtoList, projectElementDtoList)));
    }

    @Operation(
            summary = "Project 생성하기",
            description = "간단하게 title, description, UploadFile 만으로 생성합니다. <br />" +
                    "이후 Project 내의 세부사항은 update 요청으로 진행합니다."
    )
    @PostMapping
    public ResponseEntity<ResultDto<ProjectSimpleDto>> createProject(
            @RequestParam("aui") String aui,
            @Valid @RequestBody CreateProjectReq createProjectReq
    ){
        log.info("hello from createProject");
        Member loginUser = authService.getMemberFromContext();
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        Project createdProject = projectService.createProject(
                loginUser,
                createProjectReq.getOriginUrl(),
                createProjectReq.getThumbnailUrl(),
                createProjectReq.getTitle(),
                createProjectReq.getDescription()
        );

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
    @Operation(
            summary = "Project 수정하기",
            description = "projectInfo 관련 설명 <br/><br/>" +
                    "Project 내에 ProjectInfo 생성, 수정, 삭제를 위해 3가지로 나뉩니다. <br />" +
                    "createdProjectInfoList: 생성된 ProjectInfo <br/>" +
                    "updatedProjectInfoList: 생성된 ProjectInfo <br/>" +
                    "removedProjectInfoList: 삭제된 ProjectInfo <br/><br/>" +
                    "Project 삭제 시 isDeleted 를 true 로 변경합니다. <br/><br/>" +
                    "projectElement는 전용 API를 사용합니다."
    )
    @PutMapping
    public ResponseEntity<ResultDto<ProjectDto>> updateProject(
            @RequestParam("aui") String aui,
            @Valid @RequestBody UpdateProjectReq updateProjectReq
    ){
        log.info("hello from updateProject");
        Member loginUser = authService.getMemberFromContext();
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        //project 업데이트
        Project updatedProject = projectService.updateProject(
                updateProjectReq.getId(),
                updateProjectReq.getOriginUrl(),
                updateProjectReq.getThumbnailUrl(),
                updateProjectReq.getTitle(),
                updateProjectReq.getDescription(),
                updateProjectReq.getStartDate(),
                updateProjectReq.getEndDate(),
                updateProjectReq.getSupportedBy(),
                updateProjectReq.getIsDeleted()
        );

        //projectInfo 업데이트
        updateProjectInfo(updatedProject,
                updateProjectReq.getCreatedProjectInfoList(),
                updateProjectReq.getUpdatedProjectInfoList(),
                updateProjectReq.getRemovedProjectInfoList());

        List<ProjectInfo> projectInfoList = projectInfoService.findProjectInfoByProject(updatedProject);

        List<ProjectInfoDto> projectInfoDtoList = projectInfoList.stream()
                .map((p) -> new ProjectInfoDto(p))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectDto(updatedProject, projectInfoDtoList)));
    }

    private void updateProjectInfo(Project targetProject,
                       List<CreateProjectInfoReq> createdList,
                       List<UpdateProjectInfoReq> updatedList,
                       List<RemoveProjectInfoReq> removedList
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
                .forEach((p) -> projectInfoService.removeProjectInfo(p.getId()));
    }
}

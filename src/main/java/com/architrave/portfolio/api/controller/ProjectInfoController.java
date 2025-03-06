package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.project.request.*;
import com.architrave.portfolio.api.dto.project.response.ProjectInfoDto;
import com.architrave.portfolio.api.service.ProjectInfoService;
import com.architrave.portfolio.api.service.ProjectService;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "04. ProjectInfo")  // => swagger 이름
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project-info")
public class ProjectInfoController {
    private final ProjectService projectService;
    private final ProjectInfoService projectInfoService;

    @Operation(summary = "ProjectId로 작가의 ProjectInfo List 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<List<ProjectInfoDto>>> getProjectInfoList(
            @RequestParam("aui") String aui,
            @RequestParam("projectId") Long projectId
    ){
        Project project = projectService.findById(projectId);
        List<ProjectInfo> projectInfoList = projectInfoService.findProjectInfoByProject(project);
        List<ProjectInfoDto> result = projectInfoList.stream()
                .map((pi) -> new ProjectInfoDto(pi))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(result));
    }

    @Operation(summary = "ProjectInfo 생성하기")
    @PostMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<ProjectInfoDto>> createProjectInfo(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody CreateProjectInfoReq createProjectInfoReq
    ){
        Project project = projectService.findById(createProjectInfoReq.getProjectId());

        ProjectInfo projectInfo = projectInfoService.createProjectInfo(project,
                createProjectInfoReq.getCustomName(),
                createProjectInfoReq.getCustomValue()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectInfoDto(projectInfo)));
    }

    @Operation(summary = "ProjectInfo 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<ProjectInfoDto>> updateProjectInfo(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateProjectInfoReq updateProjectInfoReq
    ){
        //projectInfo 업데이트
        ProjectInfo projectInfo = projectInfoService.updateProjectInfo(
                updateProjectInfoReq.getId(),
                updateProjectInfoReq.getCustomName(),
                updateProjectInfoReq.getCustomValue()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectInfoDto(projectInfo)));
    }


    @Operation(summary = "ProjectInfo 삭제하기")
    @DeleteMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<String>> removeProjectInfo(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @RequestParam("projectInfoId") Long targetId
    ){
        projectInfoService.removeProjectInfoById(targetId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("delete project success"));
    }
}

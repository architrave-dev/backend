package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.project.request.*;
import com.architrave.portfolio.api.dto.project.response.ProjectDto;
import com.architrave.portfolio.api.dto.project.response.ProjectSimpleDto;
import com.architrave.portfolio.api.dto.reorder.request.ReorderReq;
import com.architrave.portfolio.api.dto.reorder.request.UpdateReorderListReq;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Tag(name = "03. Project")  // => swagger 이름
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final ProjectInfoService projectInfoService;
    private final ProjectElementService projectElementService;
    private final OwnerContextHolder ownerContextHolder;

    @Operation(summary = "작가의 Project List 조회하기")
    @GetMapping("/list")
    public ResponseEntity<ResultDto<List<ProjectSimpleDto>>> getProjectList(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);
        List<Project> projectList = projectService.findByMemberOrderByIndex(member);
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
            @RequestParam("projectId") Long projectId
    ){
        Project project = projectService.findById(projectId);
        if(!project.getMember().getAui().equals(aui)){
            throw new NoSuchElementException("project and member mismatch");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectDto(project)));
    }

    @Operation(
            summary = "Project 생성하기",
            description = "간단하게 title, description, UploadFile 만으로 생성합니다. <br />" +
                    "이후 Project 내의 세부사항은 update 요청으로 진행합니다."
    )
    @PostMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<ProjectSimpleDto>> createProject(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody CreateProjectReq createProjectReq
    ){
        Member owner = ownerContextHolder.getOwner();

        Project createdProject = projectService.createProject(
                owner,
                createProjectReq.getOriginUrl(),
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
    @Operation(summary = "Project 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<ProjectDto>> updateProject(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateProjectReq updateProjectReq
    ){
        //project 업데이트
        Project updatedProject = projectService.updateProject(
                updateProjectReq.getId(),
                updateProjectReq.getUpdateUploadFileReq().getOriginUrl(),
                updateProjectReq.getTitle(),
                updateProjectReq.getDescription()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectDto(updatedProject)));
    }

    @Operation(summary = "Project 순서 수정하기")
    @PutMapping("/reorder")
    @OwnerCheck
    public ResponseEntity<ResultDto<List<ProjectSimpleDto>>> reorderProjectList(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateReorderListReq updateReorderListReq
    ){
        Member owner = ownerContextHolder.getOwner();
        List<ReorderReq> reorderReqList = updateReorderListReq.getReorderReqList();

        List<Project> reorderedProjectList = projectService.reorder(owner, reorderReqList);
        List<ProjectSimpleDto> result = reorderedProjectList.stream()
                .map((p) -> new ProjectSimpleDto(p))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(result));
    }

    @Operation(
            summary = "Project 삭제하기",
            description = "Project가 삭제되면 " +
                    "관련된 ProjectInfo와 " +
                    "ProjectElement가 모두 삭제됩니다."
    )
    @DeleteMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<String>> removeProject(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @RequestParam("projectId") Long targetId
    ){
        Project project = projectService.findById(targetId);
        //projectInfo 삭제
        projectInfoService.removeProjectInfoByProject(project);
        //projectElement 삭제
        projectElementService.removeProjectElementByProject(project);
        //project 삭제
        projectService.removeProject(project);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("delete project success"));
    }
}

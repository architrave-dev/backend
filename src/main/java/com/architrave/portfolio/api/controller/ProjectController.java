package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.project.request.*;
import com.architrave.portfolio.api.dto.project.response.ProjectDto;
import com.architrave.portfolio.api.dto.project.response.ProjectInfoDto;
import com.architrave.portfolio.api.dto.project.response.ProjectSimpleDto;
import com.architrave.portfolio.api.dto.projectElement.request.IndexDto;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
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
import java.util.NoSuchElementException;
import java.util.Optional;
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
            @RequestParam("projectId") Long projectId
    ){
        Project project = projectService.findById(projectId);
        if(!project.getMember().getAui().equals(aui)){
            throw new NoSuchElementException("project and member mismatch");
        }
        List<ProjectInfo> projectInfoList = projectInfoService.findProjectInfoByProject(project);

        List<ProjectInfoDto> projectInfoDtoList = projectInfoList.stream()
                .map((pi) -> new ProjectInfoDto(pi))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectDto(project, projectInfoDtoList)));
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
    @Operation(
            summary = "Project 수정하기",
            description = "projectInfo 관련 설명 <br/><br/>" +
                    "Project 내에 ProjectInfo 생성, 수정, 삭제를 위해 3가지로 나뉩니다. <br />" +
                    "createdProjectInfoList: 생성된 ProjectInfo <br/>" +
                    "updatedProjectInfoList: 생성된 ProjectInfo <br/>" +
                    "removedProjectInfoList: 삭제된 ProjectInfo <br/><br/>" +
                    "projectElement는 전용 API를 사용합니다."
    )
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

        //projectInfo 업데이트
        List<IndexDto> indexDtoList = updateProjectInfo(updatedProject,
                updateProjectReq.getCreatedProjectInfoList(),
                updateProjectReq.getUpdatedProjectInfoList(),
                updateProjectReq.getRemovedProjectInfoList(),
                updateProjectReq.getPiIndexList());

        String piIndex = convertToStringUsingMap(indexDtoList);
        Project lastProject = projectService.updatePiIndex(updateProjectReq.getId(), piIndex);

        List<ProjectInfo> projectInfoList = projectInfoService.findProjectInfoByProject(lastProject);

        List<ProjectInfoDto> projectInfoDtoList = projectInfoList.stream()
                .map((p) -> new ProjectInfoDto(p))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectDto(lastProject, projectInfoDtoList)));
    }

    private String convertToStringUsingMap(List<IndexDto> indexDtoList) {
        return indexDtoList.stream()
                .map(dto -> Optional.ofNullable(dto.getId())
                        .orElseThrow(() -> new IllegalStateException("Id is null for IndexDto")))
                .map(Object::toString)
                .collect(Collectors.joining("_"));
    }

    private List<IndexDto> updateProjectInfo(Project targetProject,
                                             List<CreateProjectInfoReq> createdList,
                                             List<UpdateProjectInfoReq> updatedList,
                                             List<RemoveProjectInfoReq> removedList,
                                             List<IndexDto> indexDtoList
    ){
        createdList.forEach(pi -> {
            ProjectInfo projectInfo = projectInfoService.createProjectInfo(
                    targetProject,
                    pi.getCustomName(),
                    pi.getCustomValue());
            Long tempId = pi.getTempId();
            Long piId = projectInfo.getId();
            indexDtoList.stream()
                    .filter(idxDto -> {
                        Long tempPiId = idxDto.getTempId();
                        return tempPiId != null && tempPiId.equals(tempId);
                    })
                    .forEach(idxDto -> idxDto.setId(piId));
        });

        updatedList.forEach((pi) -> projectInfoService.updateProjectInfo(
                        pi.getId(),
                        pi.getCustomName(),
                        pi.getCustomValue()));

        removedList.forEach((pi) -> projectInfoService.removeProjectInfoById(pi.getId()));

        return indexDtoList;
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

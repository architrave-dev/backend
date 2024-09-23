package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.projectElement.request.*;
import com.architrave.portfolio.api.dto.projectElement.response.ProjectElementDto;
import com.architrave.portfolio.api.dto.projectElement.response.ProjectElementListDto;
import com.architrave.portfolio.api.dto.textBox.request.UpdateTextBoxReq;
import com.architrave.portfolio.api.dto.work.request.CreateWorkReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.DividerInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.TextBoxInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.WorkInProjectBuilder;
import com.architrave.portfolio.domain.model.enumType.ProjectElementType;
import com.architrave.portfolio.global.aop.Trace;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "4. ProjectElement")  // => swagger 이름
@Slf4j
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project-element")
public class ProjectElementController {

    private final ProjectElementService projectElementService;
    private final ProjectService projectService;
    private final WorkService workService;
    private final TextBoxService textBoxService;
    private final MemberService memberService;
    private final AuthService authService;

    @Operation(summary = "작가의 특정 Project의 ProjectElement List 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<ProjectElementListDto>> getProjectElementList(
            @RequestParam("aui") String aui,
            @RequestParam("projectTitle") String projectTitle
    ){
        log.info("hello from getProjectElementList");

        Member member = memberService.findMemberByAui(aui);
        Project project = projectService.findByMemberAndTitle(member, projectTitle);
        List<ProjectElement> projectElementList = projectElementService.findProjectElementByProject(project);
        //peIndex 처리할 필요 없음, 프론트에서 없으면 그냥 무시해
        List<ProjectElementDto> projectElementDtoList = projectElementList.stream()
                .map((pe) -> new ProjectElementDto(pe))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(
                        new ProjectElementListDto(
                                project.getPeIndex(),
                                projectElementDtoList
                        )
                ));
    }

    @Operation(summary = "특정 Project 내의 ProjectElement List 수정하기",
            description = "한번의 요청으로 다음의 것들을 처리합니다." +
                    "1. 새롭게 추가되는 ProjectElement 리스트" +
                    "2. 기존 ProjectElement 변경 리스트" +
                    "3. 삭제되는 ProjectElement 리스트를 받습니다."
    )
    @PutMapping
    public ResponseEntity<ResultDto<ProjectElementListDto>> updateProjectElementList(
            @RequestParam("aui") String aui,
            @Valid @RequestBody UpdateProjectElementListReq updateProjectElementListReq
    ) {
        log.info("hello from updateProjectElementList");
        Member loginUser = authService.getMemberFromContext();
        if (!loginUser.getAui().equals(aui)) {
            throw new UnauthorizedException("loginUser is not page owner");
        }

        //projectElementList 업데이트
        List<IndexDto> indexDtoList = updateProjectElementList(loginUser,
                updateProjectElementListReq.getCreateProjectElements(),
                updateProjectElementListReq.getUpdatedProjectElements(),
                updateProjectElementListReq.getRemovedProjectElements(),
                updateProjectElementListReq.getPeIndexList()
        );

        String peIndex = convertToStringUsingMap(indexDtoList);
        Project targetProject = projectService.updatePeIndex(updateProjectElementListReq.getProjectId(), peIndex);

        List<ProjectElement> projectElementList = projectElementService.findProjectElementByProject(targetProject);

        List<ProjectElementDto> projectElementDtoList = projectElementList.stream()
                .map((pe) -> new ProjectElementDto(pe))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(
                        new ProjectElementListDto(
                                peIndex,
                                projectElementDtoList
                )));
    }

    private String convertToStringUsingMap(List<IndexDto> indexDtoList) {
        return indexDtoList.stream()
                .map(dto -> Optional.ofNullable(dto.getId())
                        .orElseThrow(() -> new IllegalStateException("ProjectId is null for IndexDto")))
                .map(Object::toString)
                .collect(Collectors.joining("_"));
    }

    private List<IndexDto> updateProjectElementList(Member loginUser,
                                   List<CreateProjectElementReq> createdList,
                                   List<UpdateProjectElementReq> updatedList,
                                   List<RemoveProjectElementReq> removedList,
                                          List<IndexDto> indexDtoList
    ){
        createdList.forEach(pe -> {
            ProjectElement projectElement = projectElementService.createProjectElement(
                    handleProjectElement(loginUser, pe));
            Long tempId = pe.getTempId();
            Long peId = projectElement.getId();
            indexDtoList.stream()
                    .filter(idxDto -> {
                        Long tempPeId = idxDto.getTempId();
                        return tempPeId != null && tempPeId.equals(tempId);
                    })
                    .forEach(idxDto -> idxDto.setId(peId));
        });
        updatedList.forEach(this::handleUpdateProjectElement);
        removedList.forEach(p -> projectElementService.removeById(p.getId()));

        return indexDtoList;
    }

//    /**
//     * 향후에 변경되더라도
//     * 현재는 project 내의 projectElement 변경사항 쿼리를 한 번에 보내지 않는다.
//     * projectElment를 하나씩 보내기 때문에 단건 create
//     */
//    @Operation(summary = "특정 Project 내의 ProjectElement 생성하기")
//    @Deprecated
//    @PostMapping
//    public ResponseEntity<ResultDto<ProjectElementDto>> createProjectElement(
//            @RequestParam("aui") String aui,
//            @Valid @RequestBody CreateProjectElementReq createProjectElementReq
//    ){
//        log.info("hello from createProjectElement");
//        Member loginUser = authService.getMemberFromContext();
//        if(!loginUser.getAui().equals(aui)){
//            throw new UnauthorizedException("loginUser is not page owner");
//        }
//
//        ProjectElement projectElement = handleProjectElement(loginUser, createProjectElementReq);
//        ProjectElement createdProjectElement = projectElementService.createProjectElement(
//                projectElement);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new ResultDto<>(new ProjectElementDto(createdProjectElement)));
//    }
//
//    @Operation(summary = "특정 Project 내의 ProjectElement (Work) 수정하기",
//            description = "ProjectElement는 Work, TextBox, Divider 3가지 유형이 있습니다. <br />" +
//                    "수정 대상의 유형에 따라 다른 update 요청을 보내야 합니다. <br />" +
//                    "이는 향후 ProjectElement의 유형이 추가되거나 일괄변경 로직이 생길 경우 통합될 가능성이 있습니다."
//    )
//    @Deprecated
//    @PutMapping("/work")
//    public ResponseEntity<ResultDto<ProjectElementDto>> updateWorkProjectElement(
//            @RequestParam("aui") String aui,
//            @Valid @RequestBody UpdateWorkProjectElementReq updateWorkProjectElementReq
//    ) {
//        log.info("hello from updateWorkProjectElement");
//        Member loginUser = authService.getMemberFromContext();
//        if (!loginUser.getAui().equals(aui)) {
//            throw new UnauthorizedException("loginUser is not page owner");
//        }
//
//        UpdateWorkReq updateWorkReq = updateWorkProjectElementReq.getUpdateWorkReq();
//        //work update 먼저 하고
//        Work updatedWork = workService.updateWork(
//                updateWorkReq.getId(),
//                updateWorkReq.getOriginUrl(),
//                updateWorkReq.getThumbnailUrl(),
//                updateWorkReq.getTitle(),
//                updateWorkReq.getDescription(),
//                updateWorkReq.getSize(),
//                updateWorkReq.getMaterial(),
//                updateWorkReq.getProdYear()
//        );
//
//        // updated 된 work를 전달
//        ProjectElement projectElement = projectElementService.updateProjectElementWork(
//                updatedWork,
//                updateWorkProjectElementReq.getId(),
//                updateWorkProjectElementReq.getWorkAlignment()
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new ResultDto<>(new ProjectElementDto(projectElement)));
//    }
//
//    @Operation(
//            summary = "특정 Project 내의 ProjectElement (TextBox) 수정하기",
//            description = "ProjectElement는 Work, TextBox, Divider 3가지 유형이 있습니다. <br />" +
//                    "수정 대상의 유형에 따라 다른 update 요청을 보내야 합니다. <br />" +
//                    "이는 향후 ProjectElement의 유형이 추가되거나 일괄변경 로직이 생길 경우 통합될 가능성이 있습니다."
//    )
//    @Deprecated
//    @PutMapping("/textBox")
//    public ResponseEntity<ResultDto<ProjectElementDto>> updateTextBoxProjectElement(
//            @RequestParam("aui") String aui,
//            @Valid @RequestBody UpdateTextBoxProjectElementReq updateTextBoxProjectElementReq
//    ) {
//        log.info("hello from updateTextBoxProjectElement");
//        Member loginUser = authService.getMemberFromContext();
//        if (!loginUser.getAui().equals(aui)) {
//            throw new UnauthorizedException("loginUser is not page owner");
//        }
//        UpdateTextBoxReq updateTextBoxReq = updateTextBoxProjectElementReq.getUpdateTextBoxReq();
//
//        TextBox updatedTextBox = textBoxService.updateTextBox(
//                updateTextBoxReq.getId(),
//                updateTextBoxReq.getContent()
//        );
//
//        // updated 된 textBox 를 전달
//        ProjectElement projectElement = projectElementService.updateProjectElementTextBox(
//                updatedTextBox,
//                updateTextBoxProjectElementReq.getId(),
//                updateTextBoxProjectElementReq.getTextBoxAlignment()
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new ResultDto<>(new ProjectElementDto(projectElement)));
//    }
//
//    @Operation(summary = "특정 Project 내의 ProjectElement (Divider) 수정하기" ,
//            description = "ProjectElement는 Work, TextBox, Divider 3가지 유형이 있습니다. <br />" +
//                    "수정 대상의 유형에 따라 다른 update 요청을 보내야 합니다. <br />" +
//                    "이는 향후 ProjectElement의 유형이 추가되거나 일괄변경 로직이 생길 경우 통합될 가능성이 있습니다."
//    )
//    @Deprecated
//    @PutMapping("/divider")
//    public ResponseEntity<ResultDto<ProjectElementDto>> updateDividerProjectElement(
//            @RequestParam("aui") String aui,
//            @Valid @RequestBody UpdateDividerProjectElementReq updateDividerProjectElementReq
//    ) {
//        log.info("hello from updateDividerProjectElement");
//        Member loginUser = authService.getMemberFromContext();
//        if (!loginUser.getAui().equals(aui)) {
//            throw new UnauthorizedException("loginUser is not page owner");
//        }
//
//        ProjectElement projectElement = projectElementService.updateProjectElementDivider(
//                updateDividerProjectElementReq.getId(),
//                updateDividerProjectElementReq.getDividerType()
//        );
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new ResultDto<>(new ProjectElementDto(projectElement)));
//    }
//
//    /**
//     * 향후에 변경되더라도
//     * 현재는 project 내의 projectElement 변경사항 쿼리를 한 번에 보내지 않는다.
//     * projectElment를 하나씩 보내기 때문에 단건 delete
//     */
//    @Operation(summary = "특정 Project 내의 ProjectElement 삭제하기")
//    @Deprecated
//    @DeleteMapping
//    public ResponseEntity<ResultDto<String>> deleteProjectElement(
//            @RequestParam("aui") String aui,
//            @Valid @RequestBody RemoveProjectElementReq removeProjectElementReq
//    ){
//        log.info("hello from deleteProjectElement");
//        Member loginUser = authService.getMemberFromContext();
//        if(!loginUser.getAui().equals(aui)){
//            throw new UnauthorizedException("loginUser is not page owner");
//        }
//        projectElementService.removeById(removeProjectElementReq.getId());
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(new ResultDto<>("delete success"));
//    }
//
//    /**
//     * 전체 순서변경을 지금 만들어야하나...?
//     * 프론트에게 미안하더라도 일단 하나씩 하자.
//     */
//    @Operation(
//            summary = "[미지원] 특정 Project 내의 ProjectElement 간 순서 일괄 변경",
//            description = "현재까지는 프론트에서 순서 계산 후 개별로 update 요청"
//    )
//    @Deprecated
//    @GetMapping("/order")
//    private void changeOrderAtOnce(){}
//

    private ProjectElement handleProjectElement(Member loginUser, CreateProjectElementReq createProjectElementReq) {
        Project project = projectService.findById(createProjectElementReq.getProjectId());
        ProjectElementType elementType = createProjectElementReq.getProjectElementType();
        ProjectElement projectElement = null;

        if(elementType.equals(ProjectElementType.WORK)){
            CreateWorkReq createWorkReq = createProjectElementReq.getCreateWorkReq();
            Work work = workService.createWork(
                    loginUser,
                    createWorkReq.getOriginUrl(),
                    createWorkReq.getThumbnailUrl(),
                    createWorkReq.getTitle(),
                    createWorkReq.getDescription(),
                    createWorkReq.getSize(),
                    createWorkReq.getMaterial(),
                    createWorkReq.getProdYear());
            projectElement = new WorkInProjectBuilder()
                    .project(project)
                    .work(work)
                    .workAlignment(createProjectElementReq.getWorkAlignment())
                    .workDisplaySize(createProjectElementReq.getWorkDisplaySize())
                    .build();
        }else if(elementType.equals(ProjectElementType.TEXTBOX)){
            TextBox textBox = textBoxService.createTextBox(
                    createProjectElementReq
                            .getCreateTextBoxReq()
                            .getContent()
            );
            projectElement = new TextBoxInProjectBuilder()
                    .project(project)
                    .textBox(textBox)
                    .textBoxAlignment(createProjectElementReq.getTextBoxAlignment())
                    .build();
        }else if(elementType.equals(ProjectElementType.DIVIDER)){
            projectElement = new DividerInProjectBuilder()
                    .project(project)
                    .dividerType(createProjectElementReq.getDividerType())
                    .build();
        }
        return projectElement;
    }

    private void handleUpdateProjectElement(UpdateProjectElementReq updateProjectElementReq){
        //work일 경우
        if(!(updateProjectElementReq.getUpdateWorkReq() == null &&
                updateProjectElementReq.getWorkAlignment() == null &&
                updateProjectElementReq.getWorkDisplaySize() == null)
        ) {
            UpdateWorkReq updateWorkReq = updateProjectElementReq.getUpdateWorkReq();
            Work updatedWork = workService.updateWork(
                    updateWorkReq.getId(),
                    updateWorkReq.getOriginUrl(),
                    updateWorkReq.getThumbnailUrl(),
                    updateWorkReq.getTitle(),
                    updateWorkReq.getDescription(),
                    updateWorkReq.getSize(),
                    updateWorkReq.getMaterial(),
                    updateWorkReq.getProdYear()
            );
            // updated 된 work를 전달
            projectElementService.updateProjectElementWork(
                    updatedWork,
                    updateProjectElementReq.getId(),
                    updateProjectElementReq.getWorkAlignment(),
                    updateProjectElementReq.getWorkDisplaySize()
            );
        }
        //textBox일 경우
        else if(updateProjectElementReq.getUpdateTextBoxReq() != null)
        {
            UpdateTextBoxReq updateTextBoxReq = updateProjectElementReq.getUpdateTextBoxReq();
            TextBox updatedTextBox = textBoxService.updateTextBox(
                    updateTextBoxReq.getId(),
                    updateTextBoxReq.getContent()
            );
            // updated 된 textBox 를 전달
            projectElementService.updateProjectElementTextBox(
                    updatedTextBox,
                    updateProjectElementReq.getId(),
                    updateProjectElementReq.getTextBoxAlignment()
            );
        }
        //divider 일 경우
        else{
            projectElementService.updateProjectElementDivider(
                    updateProjectElementReq.getId(),
                    updateProjectElementReq.getDividerType()
            );
        }
    }
}

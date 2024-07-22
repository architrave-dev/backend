package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.projectElement.request.*;
import com.architrave.portfolio.api.dto.projectElement.response.ProjectElementDto;
import com.architrave.portfolio.api.dto.textBox.request.UpdateTextBoxReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.DividerInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.TextBoxInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.WorkInProjectBuilder;
import com.architrave.portfolio.domain.model.enumType.ProjectElementType;
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
@RequestMapping("/api/v1/project-element")
public class ProjectElementController {

    private final ProjectElementService projectElementService;
    private final ProjectService projectService;
    private final WorkService workService;
    private final TextBoxService textBoxService;
    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<ResultDto<List<ProjectElementDto>>> getProjectElementList(
            @RequestParam("aui") String aui,
            @RequestParam("projectTitle") String projectTitle
    ){
        log.info("hello from getProjectElementList");

        Member member = memberService.findMemberByAui(aui);
        Project project = projectService.findByMemberAndTitle(member, projectTitle);
        List<ProjectElement> projectElementList = projectElementService.findProjectElementByProject(project);

        List<ProjectElementDto> projectElementDtoList = projectElementList.stream()
                .map((pe) -> new ProjectElementDto(pe))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(projectElementDtoList));
    }

    /**
     * 향후에 변경되더라도
     * 현재는 project 내의 projectElement 변경사항 쿼리를 한 번에 보내지 않는다.
     * projectElment를 하나씩 보내기 때문에 단건 create
     */
    @PostMapping
    public ResponseEntity<ResultDto<ProjectElementDto>> createProjectElement(
            @RequestParam("aui") String aui,
            @RequestBody CreateProjectElementReq createProjectElementReq
    ){
        log.info("hello from createProjectElement");
        Member loginUser = authService.getMemberFromContext();
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        ProjectElement projectElement = handleProjectElement(createProjectElementReq);
        ProjectElement createdProjectElement = projectElementService.createProjectElement(
                projectElement);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectElementDto(createdProjectElement)));
    }

    @PutMapping("/work")
    public ResponseEntity<ResultDto<ProjectElementDto>> updateWorkProjectElement(
            @RequestParam("aui") String aui,
            @RequestBody UpdateWorkProjectElementReq updateWorkProjectElementReq
    ) {
        log.info("hello from updateWorkProjectElement");
        Member loginUser = authService.getMemberFromContext();
        if (!loginUser.getAui().equals(aui)) {
            throw new UnauthorizedException("loginUser is not page owner");
        }

        UpdateWorkReq updateWorkReq = updateWorkProjectElementReq.getUpdateWorkReq();
        //work update 먼저 하고
        Work updatedWork = workService.updateWork(
                updateWorkReq.getId(),
                updateWorkReq.getOriginImgUrl(),
                updateWorkReq.getThumbnailUrl(),
                updateWorkReq.getTitle(),
                updateWorkReq.getDescription(),
                updateWorkReq.getSize(),
                updateWorkReq.getMaterial(),
                updateWorkReq.getProdYear(),
                updateWorkReq.getIsDeleted()
        );

        // updated 된 work를 전달
        ProjectElement projectElement = projectElementService.updateProjectElementWork(
                updatedWork,
                updateWorkProjectElementReq.getId(),
                updateWorkProjectElementReq.getWorkAlignment(),
                updateWorkProjectElementReq.getPeOrder(),
                updateWorkProjectElementReq.getIsRepresentative()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectElementDto(projectElement)));
    }

    @PutMapping("/textBox")
    public ResponseEntity<ResultDto<ProjectElementDto>> updateTextBoxProjectElement(
            @RequestParam("aui") String aui,
            @RequestBody UpdateTextBoxProjectElementReq updateTextBoxProjectElementReq
    ) {
        log.info("hello from updateTextBoxProjectElement");
        Member loginUser = authService.getMemberFromContext();
        if (!loginUser.getAui().equals(aui)) {
            throw new UnauthorizedException("loginUser is not page owner");
        }
        UpdateTextBoxReq updateTextBoxReq = updateTextBoxProjectElementReq.getUpdateTextBoxReq();

        TextBox updatedTextBox = textBoxService.updateTextBox(
                updateTextBoxReq.getId(),
                updateTextBoxReq.getContent(),
                updateTextBoxReq.getIsDeleted()
        );

        // updated 된 textBox 를 전달
        ProjectElement projectElement = projectElementService.updateProjectElementTextBox(
                updatedTextBox,
                updateTextBoxProjectElementReq.getId(),
                updateTextBoxProjectElementReq.getTextBoxAlignment(),
                updateTextBoxProjectElementReq.getPeOrder()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectElementDto(projectElement)));
    }

    @PutMapping("/divider")
    public ResponseEntity<ResultDto<ProjectElementDto>> updateDividerProjectElement(
            @RequestParam("aui") String aui,
            @RequestBody UpdateDividerProjectElementReq updateDividerProjectElementReq
    ) {
        log.info("hello from updateDividerProjectElement");
        Member loginUser = authService.getMemberFromContext();
        if (!loginUser.getAui().equals(aui)) {
            throw new UnauthorizedException("loginUser is not page owner");
        }

        ProjectElement projectElement = projectElementService.updateProjectElementDivider(
                updateDividerProjectElementReq.getId(),
                updateDividerProjectElementReq.getDividerType(),
                updateDividerProjectElementReq.getPeOrder()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectElementDto(projectElement)));
    }

    /**
     * 향후에 변경되더라도
     * 현재는 project 내의 projectElement 변경사항 쿼리를 한 번에 보내지 않는다.
     * projectElment를 하나씩 보내기 때문에 단건 delete
     */
    @DeleteMapping
    public ResponseEntity<ResultDto<String>> deleteProjectElement(
            @RequestParam("aui") String aui,
            @RequestBody RemoveProjectElementReq removeProjectElementReq
    ){
        log.info("hello from deleteProjectElement");
        Member loginUser = authService.getMemberFromContext();
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }
        projectElementService.removeById(removeProjectElementReq.getId());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("delete success"));
    }

    /**
     * 전체 순서변경을 지금 만들어야하나...?
     * 프론트에게 미안하더라도 일단 하나씩 하자.
     */
    private void changeOrderAtOnce(){}


    private ProjectElement handleProjectElement(CreateProjectElementReq createProjectElementReq) {
        Project project = projectService.findById(createProjectElementReq.getProjectId());
        ProjectElementType elementType = createProjectElementReq.getProjectElementType();
        ProjectElement projectElement = null;

        if(elementType.equals(ProjectElementType.WORK)){
            Work work = workService.findWorkById(createProjectElementReq.getWorkId());
            projectElement = new WorkInProjectBuilder()
                    .project(project)
                    .work(work)
                    .workAlignment(createProjectElementReq.getWorkAlignment())
                    .peOrder(createProjectElementReq.getPeOrder())
                    .isRepresentative(createProjectElementReq.getIsRepresentative())
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
                    .peOrder(createProjectElementReq.getPeOrder())
                    .build();
        }else if(elementType.equals(ProjectElementType.DIVIDER)){
            projectElement = new DividerInProjectBuilder()
                    .project(project)
                    .dividerType(createProjectElementReq.getDividerType())
                    .peOrder(createProjectElementReq.getPeOrder())
                    .build();
        }
        return projectElement;
    }
}

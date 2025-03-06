package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.document.request.CreateDocumentReq;
import com.architrave.portfolio.api.dto.document.request.UpdateDocumentReq;
import com.architrave.portfolio.api.dto.projectElement.request.*;
import com.architrave.portfolio.api.dto.projectElement.response.ProjectElementDto;
import com.architrave.portfolio.api.dto.projectElement.response.ProjectElementListDto;
import com.architrave.portfolio.api.dto.textBox.request.UpdateTextBoxReq;
import com.architrave.portfolio.api.dto.work.request.CreateWorkDetailReq;
import com.architrave.portfolio.api.dto.work.request.CreateWorkReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkDetailReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.*;
import com.architrave.portfolio.domain.model.enumType.ProjectElementType;
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
import java.util.stream.Collectors;

@Tag(name = "05. ProjectElement")  // => swagger 이름
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/project-element")
public class ProjectElementController {

    private final ProjectElementService projectElementService;
    private final ProjectService projectService;
    private final WorkService workService;
    private final WorkDetailService workDetailService;
    private final TextBoxService textBoxService;
    private final DocumentService documentService;
    private final MemberService memberService;
    private final OwnerContextHolder ownerContextHolder;

    @Operation(summary = "작가의 특정 Project의 ProjectElement List 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<ProjectElementListDto>> getProjectElementList(
            @RequestParam("aui") String aui,
            @RequestParam("projectId") Long projectId
    ){
        Member member = memberService.findMemberByAui(aui);
        Project project = projectService.findByMemberAndProjectId(member, projectId);
        List<ProjectElement> projectElementList = projectElementService.findProjectElementByProject(project);

        List<ProjectElementDto> projectElementDtoList = projectElementList.stream()
                .map((pe) -> new ProjectElementDto(pe))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(
                        new ProjectElementListDto(
                                projectElementDtoList
                        )
                ));
    }

    /**
     * Import ProjectElement with Work
     * 기 존재하는 WorkId를 받아서
     * ProjectElement를 생성하고 생성된 ProjectElement를 리턴한다.
     * projectElment를 하나씩 보내기 때문에 단건 create
     */
    @Operation(summary = "import한 Work로 ProjectElement 생성하기")
    @OwnerCheck
    @PostMapping("/import")
    public ResponseEntity<ResultDto<ProjectElementDto>> createProjectElementWithWork(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody CreateProjectElementWithWorkReq createProjectElementWithWorkReq
    ){
        //work를 받아오고
        Work work = workService.findWorkById(createProjectElementWithWorkReq.getWorkId());
        Project project = projectService.findById(createProjectElementWithWorkReq.getProjectId());

        ProjectElement projectElement = new WorkInProjectBuilder()
                .project(project)
                .work(work)
                .workAlignment(createProjectElementWithWorkReq.getDisplayAlignment())
                .workDisplaySize(createProjectElementWithWorkReq.getDisplaySize())
                .build();

        ProjectElement createdProjectElement = projectElementService.createProjectElement(projectElement);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectElementDto(createdProjectElement)));
    }
    /**
     * Import ProjectElement with WorkDetail
     * 기 존재하는 WorkDetailId를 받아서
     * ProjectElement를 생성하고 생성된 ProjectElement를 리턴한다.
     * projectElment를 하나씩 보내기 때문에 단건 create
     */
    @Operation(summary = "import한 WorkDetail로 ProjectElement 생성하기")
    @OwnerCheck
    @PostMapping("/import/detail")
    public ResponseEntity<ResultDto<ProjectElementDto>> createProjectElementWithWorkDetail(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody CreateProjectElementWithWorkDetailReq createProjectElementWithWorkDetailReq
    ){
        //workDetail를 받아오고
        WorkDetail workDetail = workDetailService.findWorkDetailById(createProjectElementWithWorkDetailReq.getWorkDetailId());
        Project project = projectService.findById(createProjectElementWithWorkDetailReq.getProjectId());

        ProjectElement projectElement = new WorkDetailInProjectBuilder()
                .project(project)
                .workDetail(workDetail)
                .workDetailAlignment(createProjectElementWithWorkDetailReq.getDisplayAlignment())
                .workDetailDisplaySize(createProjectElementWithWorkDetailReq.getDisplaySize())
                .build();

        ProjectElement createdProjectElement = projectElementService.createProjectElement(projectElement);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectElementDto(createdProjectElement)));
    }

    @Operation(summary = "단건 ProjectElement 생성하기")
    @OwnerCheck
    @PostMapping
    public ResponseEntity<ResultDto<ProjectElementDto>> createProjectElement(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody CreateProjectElementReq createProjectElementReq
    ){
        Member owner = ownerContextHolder.getOwner();

        ProjectElement createdProjectElement = projectElementService.createProjectElement(
                handleCreateProjectElement(owner, createProjectElementReq));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectElementDto(createdProjectElement)));
    }

    @Operation(summary = "단건 ProjectElement 수정하기")
    @OwnerCheck
    @PutMapping
    public ResponseEntity<ResultDto<ProjectElementDto>> updateProjectElement(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateProjectElementReq updateProjectElementReq
    ){
        ProjectElement updated = handleUpdateProjectElement(updateProjectElementReq);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ProjectElementDto(updated)));
    }

    @Operation(summary = "단건 ProjectElement 삭제하기")
    @OwnerCheck
    @DeleteMapping
    public ResponseEntity<ResultDto<String>> deleteProjectElement(
            @RequestParam("aui") String aui, // aop OwnerCheck 에서 사용.
            @RequestParam("peId") Long targetId
    ){
        projectElementService.removeById(targetId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("delete project-element success"));
    }

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

    private ProjectElement handleCreateProjectElement(Member loginUser, CreateProjectElementReq createProjectElementReq) {
        Project project = projectService.findById(createProjectElementReq.getProjectId());
        ProjectElementType elementType = createProjectElementReq.getProjectElementType();
        ProjectElement projectElement = null;

        if(elementType.equals(ProjectElementType.WORK)){
            CreateWorkReq createWorkReq = createProjectElementReq.getCreateWorkReq();
            Work work = workService.createWork(
                    loginUser,
                    createWorkReq.getWorkType(),
                    createWorkReq.getOriginUrl(),
                    createWorkReq.getTitle(),
                    createWorkReq.getDescription(),
                    createWorkReq.getSize(),
                    createWorkReq.getMaterial(),
                    createWorkReq.getProdYear(),
                    createWorkReq.getPrice(),
                    createWorkReq.getCollection()
            );
            projectElement = new WorkInProjectBuilder()
                    .project(project)
                    .work(work)
                    .workAlignment(createProjectElementReq.getDisplayAlignment())
                    .workDisplaySize(createProjectElementReq.getDisplaySize())
                    .build();
        }else if(elementType.equals(ProjectElementType.DETAIL)){
            CreateWorkDetailReq createWorkDetailReq = createProjectElementReq.getCreateWorkDetailReq();
            Work work = workService.findWorkById(createWorkDetailReq.getWorkId());
            WorkDetail workDetail = workDetailService.createWorkDetail(
                    work,
                    createWorkDetailReq.getOriginUrl(),
                    createWorkDetailReq.getDescription()
            );
            projectElement = new WorkDetailInProjectBuilder()
                    .project(project)
                    .workDetail(workDetail)
                    .workDetailAlignment(createProjectElementReq.getDisplayAlignment())
                    .workDetailDisplaySize(createProjectElementReq.getDisplaySize())
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
                    .textBoxAlignment(createProjectElementReq.getTextAlignment())
                    .build();
        }else if(elementType.equals(ProjectElementType.DOCUMENT)){
            CreateDocumentReq createDocumentReq = createProjectElementReq.getCreateDocumentReq();
            Document document = documentService.createDocument(
                    createDocumentReq.getOriginUrl(),
                    createDocumentReq.getDescription()
            );
            projectElement = new DocumentInProjectBuilder()
                    .project(project)
                    .document(document)
                    .documentAlignment(createProjectElementReq.getDisplayAlignment())
                    .build();
        }else if(elementType.equals(ProjectElementType.DIVIDER)){
            projectElement = new DividerInProjectBuilder()
                    .project(project)
                    .dividerType(createProjectElementReq.getDividerType())
                    .build();
        }
        return projectElement;
    }

    private ProjectElement handleUpdateProjectElement(UpdateProjectElementReq updateProjectElementReq){ //work일 경우
        ProjectElement projectElement;
        if(updateProjectElementReq.getUpdateWorkReq() != null) {
            UpdateWorkReq updateWorkReq = updateProjectElementReq.getUpdateWorkReq();
            Work updatedWork = workService.updateWork(
                    updateWorkReq.getId(),
                    updateWorkReq.getWorkType(),
                    updateWorkReq.getUpdateUploadFileReq().getOriginUrl(),
                    updateWorkReq.getTitle(),
                    updateWorkReq.getDescription(),
                    updateWorkReq.getSize(),
                    updateWorkReq.getMaterial(),
                    updateWorkReq.getProdYear(),
                    updateWorkReq.getPrice(),
                    updateWorkReq.getCollection()
            );
            // updated 된 work를 전달
            projectElement = projectElementService.updateProjectElementWork(
                    updatedWork,
                    updateProjectElementReq.getProjectElementId(),
                    updateProjectElementReq.getDisplayAlignment(),
                    updateProjectElementReq.getDisplaySize()
            );
        }
        else if(updateProjectElementReq.getUpdateWorkDetailReq() != null) { //workDetail일 경우
            UpdateWorkDetailReq updateWorkDetailReq =  updateProjectElementReq.getUpdateWorkDetailReq();
            WorkDetail updatedWorkDetail = workDetailService.updateWorkDetail(
                    updateWorkDetailReq.getId(),
                    updateWorkDetailReq.getUpdateUploadFileReq().getOriginUrl(),
                    updateWorkDetailReq.getDescription()
            );

            // updated 된 workDetail를 전달
            projectElement = projectElementService.updateProjectElementWorkDetail(
                    updatedWorkDetail,
                    updateProjectElementReq.getProjectElementId(),
                    updateProjectElementReq.getDisplayAlignment(),
                    updateProjectElementReq.getDisplaySize()
            );
        }
        else if(updateProjectElementReq.getUpdateTextBoxReq() != null)  //textBox일 경우
        {
            UpdateTextBoxReq updateTextBoxReq = updateProjectElementReq.getUpdateTextBoxReq();
            TextBox updatedTextBox = textBoxService.updateTextBox(
                    updateTextBoxReq.getId(),
                    updateTextBoxReq.getContent()
            );
            // updated 된 textBox 를 전달
            projectElement = projectElementService.updateProjectElementTextBox(
                    updatedTextBox,
                    updateProjectElementReq.getProjectElementId(),
                    updateProjectElementReq.getTextAlignment()
            );
        }
        else if(updateProjectElementReq.getUpdateDocumentReq() != null) //Document일 경우
        {
            UpdateDocumentReq updateDocumentReq = updateProjectElementReq.getUpdateDocumentReq();
            Document updatedDocument = documentService.updateDocument(
                    updateDocumentReq.getId(),
                    updateDocumentReq.getDescription(),
                    updateDocumentReq.getUpdateUploadFileReq().getOriginUrl()
            );

            // updated 된 document 를 전달
            projectElement = projectElementService.updateProjectElementDocument(
                    updatedDocument,
                    updateProjectElementReq.getProjectElementId(),
                    updateProjectElementReq.getDisplayAlignment()
            );
        }
        else{   //divider 일 경우
            projectElement = projectElementService.updateProjectElementDivider(
                    updateProjectElementReq.getProjectElementId(),
                    updateProjectElementReq.getDividerType()
            );
        }
        return projectElement;
    }
}

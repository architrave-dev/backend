package com.architrave.portfolio.api.service;

import com.architrave.portfolio.api.dto.reorder.request.ReorderReq;
import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.enumType.*;
import com.architrave.portfolio.domain.repository.ProjectElementRepository;
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
public class ProjectElementService {

    private final ProjectElementRepository projectElementRepository;

    private final TextBoxService textBoxService;
    private final DocumentService documentService;
    private final UploadFileService uploadFileService;


    @Transactional
    public ProjectElement createProjectElement(ProjectElement projectElement) {
        return projectElementRepository.save(projectElement);
    }

    @Transactional(readOnly = true)
    public List<ProjectElement> findProjectElementByProject(Project project) {
//        return projectElementRepository.findByProject(project);
        return projectElementRepository.findByProjectWithAssociations(project);
    }

    @Transactional(readOnly = true)
    public ProjectElement findById(Long id) {
        return projectElementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no project element that id: " + id));
    }
    @Transactional
    public ProjectElement updateProjectElementWork(Work work,
                                                   Long projectElementId,
                                                   DisplayAlignment displayAlignment,
                                                   DisplaySize displaySize
    ) {
        ProjectElement projectElement = findById(projectElementId);
        //work 내 변경사항은 이미 완료한 상태
        if(!projectElement.getWork().equals(work)){
           projectElement.setWork(work);
        }
        if(displayAlignment != null){
            projectElement.setDisplayAlignment(displayAlignment);
        }
        if(displaySize != null){
            projectElement.setDisplaySize(displaySize);
        }
        return projectElement;
    }
    @Transactional
    public ProjectElement updateProjectElementWorkDetail(WorkDetail workDetail,
                                               Long projectElementId,
                                               DisplayAlignment displayAlignment,
                                               DisplaySize displaySize
    ){
        ProjectElement projectElement = findById(projectElementId);
        //workDetail 내 변경사항은 이미 완료한 상태
        if(!projectElement.getWorkDetail().equals(workDetail)){
            projectElement.setWorkDetail(workDetail);
        }
        if(displayAlignment != null){
            projectElement.setDisplayAlignment(displayAlignment);
        }
        if(displaySize != null){
            projectElement.setDisplaySize(displaySize);
        }
        return projectElement;
    }

    @Transactional
    public ProjectElement updateProjectElementTextBox(TextBox textBox,
                                                      Long projectElementId,
                                                      TextAlignment textAlignment) {
        ProjectElement projectElement = findById(projectElementId);
        //textBox 내 변경사항은 이미 완료한 상태
        if(!projectElement.getTextBox().equals(textBox)){
            projectElement.setTextBox(textBox);
        }
        if(!projectElement.getTextAlignment().equals(textAlignment)){
            projectElement.setTextAlignment(textAlignment);
        }
        return projectElement;
    }
    @Transactional
    public ProjectElement updateProjectElementDocument(Document document,
                                             Long projectElementId,
                                             DisplayAlignment documentAlignment) {
        ProjectElement projectElement = findById(projectElementId);
        //document 내 변경사항은 이미 완료한 상태
        if(!projectElement.getDocument().equals(document)){
            projectElement.setDocument(document);
        }
        if(documentAlignment != null){
            projectElement.setDisplayAlignment(documentAlignment);
        }
        return projectElement;
    }

    @Transactional
    public ProjectElement updateProjectElementDivider(Long projectElementId,
                                                      DividerType dividerType) {
        ProjectElement projectElement = findById(projectElementId);
        if(!projectElement.getDividerType().equals(dividerType)){
            projectElement.setDividerType(dividerType);
        }
        return projectElement;
    }

    @Transactional
    public void removeById(Long id) {
        ProjectElement projectElement = findById(id);
        //Work는 ProjectElement가 삭제되어도 함께 삭제되지 않는다.
        //TextBox라면 삭제
        if(projectElement.getProjectElementType() == ProjectElementType.TEXTBOX){
            textBoxService.removeTextBox(projectElement.getTextBox().getId());
        }
        //Document라면 삭제
        if(projectElement.getProjectElementType() == ProjectElementType.DOCUMENT){
            documentService.removeDocument(projectElement.getDocument().getId());
        }
        //Divider는 ProjectElement와 한 몸
        projectElementRepository.delete(projectElement);
    }

    @Transactional
    public void removeProjectElementByProject(Project project) {
        List<ProjectElement> peList = findProjectElementByProject(project);
        peList.forEach(pe -> {
            if(pe.getProjectElementType().equals(ProjectElementType.DOCUMENT)){
                uploadFileService.deleteUploadFile(pe.getDocument().getUploadFile());
            }
        });

        projectElementRepository.deleteByProject(project);
    }

    @Transactional
    public void deleteByMemberAndWorkId(Member loginUser, Work work) {
        projectElementRepository.deleteByProjectMemberAndWork(loginUser, work);
    }

    @Transactional
    public void deleteByMemberAndWorkDetailId(Member loginUser, WorkDetail workDetail) {
        projectElementRepository.deleteByProjectMemberAndWorkDetail(loginUser, workDetail);
    }

    @Transactional
    public List<ProjectElement> reorder(Project project, List<ReorderReq> reorderReqList) {
        Map<Long, Integer> reorderMap = reorderReqList.stream()
                .collect(Collectors.toMap(ReorderReq::getId, ReorderReq::getIndex));

        List<ProjectElement> projectElementList = projectElementRepository.findByProject(project);

        for (ProjectElement projectElement : projectElementList) {
            Integer newIndex = reorderMap.get(projectElement.getId());
            if(newIndex != null){
                if (projectElement.getIndex() == null || !newIndex.equals(projectElement.getIndex())) {
                    projectElement.setIndex(newIndex);
                }
            }
        }

        return projectElementRepository.saveAll(projectElementList);
    }
}

package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.TextBox;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.ProjectElementType;
import com.architrave.portfolio.domain.model.enumType.TextBoxAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import com.architrave.portfolio.domain.repository.ProjectElementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProjectElementService {

    private final ProjectElementRepository projectElementRepository;

    private final TextBoxService textBoxService;

    @Transactional
    public ProjectElement createProjectElement(ProjectElement projectElement) {
        return projectElementRepository.save(projectElement);
    }

    @Transactional(readOnly = true)
    public List<ProjectElement> findProjectElementByProject(Project project) {
        return projectElementRepository.findByProject(project);
    }

    @Transactional(readOnly = true)
    public ProjectElement findById(Long id) {
        return projectElementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no project element that id: " + id));
    }
    @Transactional
    public ProjectElement updateProjectElementWork(Work work,
                                                   Long projectElementId,
                                                   WorkAlignment workAlignment) {
        ProjectElement projectElement = findById(projectElementId);
        //work 내 변경사항은 이미 완료한 상태
        if(!projectElement.getWork().equals(work)){
           projectElement.setWork(work);
        }
        if(workAlignment != null){
            projectElement.setWorkAlignment(workAlignment);
        }
        return projectElement;
    }

    @Transactional
    public ProjectElement updateProjectElementTextBox(TextBox textBox,
                                                      Long projectElementId,
                                                      TextBoxAlignment textBoxAlignment) {
        ProjectElement projectElement = findById(projectElementId);
        //textBox 내 변경사항은 이미 완료한 상태
        if(!projectElement.getTextBox().equals(textBox)){
            projectElement.setTextBox(textBox);
        }
        if(textBoxAlignment != null){
            projectElement.setTextBoxAlignment(textBoxAlignment);
        }
        return projectElement;
    }

    @Transactional
    public ProjectElement updateProjectElementDivider(Long projectElementId,
                                                      DividerType dividerType) {
        ProjectElement projectElement = findById(projectElementId);
        if(dividerType != null){
            projectElement.setDividerType(dividerType);
        }
        return projectElement;
    }

    @Transactional
    public void removeById(Long id) {
        ProjectElement projectElement = findById(id);
        if(projectElement.getProjectElementType() == ProjectElementType.TEXTBOX){
            textBoxService.removeTextBox(projectElement.getTextBox().getId());
        }
        projectElementRepository.delete(projectElement);
    }

    @Transactional
    public void organizeOrder(Project project){
        List<ProjectElement> projectElementList = findProjectElementByProject(project);
        for(int i=0; i<projectElementList.size(); i++){
            ProjectElement projectElement = projectElementList.get(i);
        }
    }
}

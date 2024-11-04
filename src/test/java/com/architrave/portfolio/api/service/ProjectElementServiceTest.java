package com.architrave.portfolio.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.WorkBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.DividerInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.TextBoxInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.WorkInProjectBuilder;
import com.architrave.portfolio.domain.model.enumType.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProjectElementServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private WorkService workService;
    @Autowired
    private TextBoxService textBoxService;
    @Autowired
    private ProjectElementService projectElementService;

    private final String TEST_MEMBER_EMAIL = "lee@gmail.com";
    private final String TEST_MEMBER_PASSWORD = "12345";
    private final String TEST_MEMBER_USERNAME = "이중섭";
    private final RoleType ROLE_USER = RoleType.USER;

    private final String TEST_PROJECT_TITLE = "test project";
    private final String TEST_WORK_TITLE = "test work";
    private final String TEST_WORK_TITLE_CHANGE = "test work changed";
    private final WorkAlignment TEST_WORK_ALIGNMENT = WorkAlignment.CENTER;
    private final WorkAlignment TEST_WORK_ALIGNMENT_CHANGE = WorkAlignment.LEFT;


    private final String TEST_TEXTBOX_CONTENT = "this is test textBox";
    private final String TEST_TEXTBOX_CONTENT_CHANGED = "this is changed test textBox";
    private final TextBoxAlignment TEST_TEXTBOX_ALIGNMENT = TextBoxAlignment.CENTER;
    private final TextBoxAlignment TEST_TEXTBOX_ALIGNMENT_CHANGE = TextBoxAlignment.LEFT;

    private final DividerType TEST_DIVIDER_TYPE = DividerType.PLAIN;

    private final String TEST_WORK_ORIGINAL_URL = "this is test orinalUrl";
    private final String TEST_WORK_THUMBNAIL_URL = "this is test thumbnailurl";

    private final String TEST_PROJECT_ORIGIN_URL = "test origin url";
    private final String TEST_PROJECT_THUMBNAIL_URL = "test thumbnail url";

    @Test
    public void getProjectElementsInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        Work work = createWork(member);
        ProjectElement workPE = createWorkProjectElement(project, work);
        TextBox textBox = createTextBox(member);
        ProjectElement textBoxPE = createTextboxProjectElement(project, textBox);
        ProjectElement dividerPE = createDividerProjectElement(project);

        projectElementService.createProjectElement(workPE);
        projectElementService.createProjectElement(textBoxPE);
        projectElementService.createProjectElement(dividerPE);

        //when
        List<ProjectElement> projectElementList = projectElementService.findProjectElementByProject(project);
        ProjectElement projectElement_0 = projectElementList.get(0);
        ProjectElement projectElement_1 = projectElementList.get(1);
        ProjectElement projectElement_2 = projectElementList.get(2);

        //then
        assertEquals(projectElementList.size(), 3);
        assertEquals(projectElement_0.getWork().getTitle(), TEST_WORK_TITLE);
        assertEquals(projectElement_1.getTextBox().getContent(), TEST_TEXTBOX_CONTENT);
        assertEquals(projectElement_2.getDividerType(), TEST_DIVIDER_TYPE);
    }

    @Test
    public void addWorkInProject(){
        //given
        Member member = createMemberInTest();
        Work work = createWork(member);
        Project project = createProjectInTest(member);
        ProjectElement workPE = createWorkProjectElement(project, work);

        //when
        projectElementService.createProjectElement(workPE);

        //then
        List<ProjectElement> peList = projectElementService.findProjectElementByProject(project);
        ProjectElement findWorkPE = peList.get(0);
        assertEquals(peList.size(), 1);
        assertNotNull(findWorkPE.getWork());
        assertNull(findWorkPE.getTextBox());
        assertNull(findWorkPE.getDividerType());
        assertEquals(findWorkPE.getWork().getTitle(), TEST_WORK_TITLE);
        assertEquals(findWorkPE.getWorkAlignment(), TEST_WORK_ALIGNMENT);
    }

    @Test
    public void addTextBoxInProject(){
        //given
        Member member = createMemberInTest();
        TextBox textBox = createTextBox(member);
        Project project = createProjectInTest(member);
        ProjectElement textBoxPE = createTextboxProjectElement(project, textBox);

        //when
        projectElementService.createProjectElement(textBoxPE);

        //then
        List<ProjectElement> peList = projectElementService.findProjectElementByProject(project);
        ProjectElement findTextBoxPE = peList.get(0);
        assertEquals(peList.size(), 1);
        assertNotNull(findTextBoxPE.getTextBox());
        assertNull(findTextBoxPE.getWork());
        assertNull(findTextBoxPE.getDividerType());
        assertEquals(findTextBoxPE.getTextBox().getContent(), TEST_TEXTBOX_CONTENT);
        assertEquals(findTextBoxPE.getTextBoxAlignment(), TEST_TEXTBOX_ALIGNMENT);
    }

    @Test
    public void addDividerInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        ProjectElement dividerPE = createDividerProjectElement(project);

        //when
        projectElementService.createProjectElement(dividerPE);

        //then
        List<ProjectElement> peList = projectElementService.findProjectElementByProject(project);
        ProjectElement findDividerPE = peList.get(0);
        assertEquals(peList.size(), 1);

        assertNotNull(findDividerPE.getDividerType());
        assertNull(findDividerPE.getWork());
        assertNull(findDividerPE.getTextBox());
        assertEquals(findDividerPE.getDividerType(), TEST_DIVIDER_TYPE);
    }

    @Test
    public void updateWorkInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        Work work = createWork(member);
        ProjectElement workPE = createWorkProjectElement(project, work);
        projectElementService.createProjectElement(workPE);

        //when
        workService.updateWork(
                1L,
                WorkType.painting_water,
                null,
                null,
                TEST_WORK_TITLE_CHANGE,
                null,
                null,
                null,
                null,
                null,
                null
        );

        projectElementService.updateProjectElementWork(
                work,
                1L,
                TEST_WORK_ALIGNMENT_CHANGE,
                null
        );

        //then
        Work findWork = workService.findWorkById(1L);
        assertEquals(findWork.getTitle(), TEST_WORK_TITLE_CHANGE);
        ProjectElement findWorkPE2 = projectElementService.findById(1L);
        assertEquals(findWorkPE2.getWork().getTitle(), TEST_WORK_TITLE_CHANGE);
        assertEquals(findWorkPE2.getWorkAlignment(), TEST_WORK_ALIGNMENT_CHANGE);
    }

    @Test
    public void updateTextBoxInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        TextBox textBox = createTextBox(member);
        ProjectElement textBoxPE = createTextboxProjectElement(project, textBox);

        projectElementService.createProjectElement(textBoxPE);

        //when
        TextBox updateTextBox = textBoxService.updateTextBox(1L, TEST_TEXTBOX_CONTENT_CHANGED);

        projectElementService.updateProjectElementTextBox(
                updateTextBox,
                1L,
                TEST_TEXTBOX_ALIGNMENT_CHANGE
        );

        //then
        TextBox findTextBox = textBoxService.findById(1l);
        assertEquals(findTextBox.getContent(), TEST_TEXTBOX_CONTENT_CHANGED);

        ProjectElement findTextBoxPE = projectElementService.findById(1L);
        assertNull(findTextBoxPE.getWork());
        assertEquals(findTextBoxPE.getTextBox().getContent(), TEST_TEXTBOX_CONTENT_CHANGED);
        assertEquals(findTextBoxPE.getTextBoxAlignment(), TEST_TEXTBOX_ALIGNMENT_CHANGE);
    }

    @Test
    public void updateDividerInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        ProjectElement dividerPE = createDividerProjectElement(project);

        projectElementService.createProjectElement(dividerPE);

        //when
        projectElementService.updateProjectElementDivider(
                1L,
                null
        );

        //then
        ProjectElement findDividerPE = projectElementService.findById(1L);
        assertNull(findDividerPE.getWork());
        assertNull(findDividerPE.getTextBox());
    }

    @Test
    public void removeWorkProjectElement(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        Work work = createWork(member);
        ProjectElement workPE = createWorkProjectElement(project, work);

        ProjectElement projectElement = projectElementService.createProjectElement(workPE);

        //when
        projectElementService.removeById(projectElement.getId());

        //then
        List<ProjectElement> projectElementList = projectElementService.findProjectElementByProject(project);
        assertEquals(projectElementList.size(), 0);
        Work findWork = workService.findWorkById(work.getId());
        assertNotNull(findWork);
        assertEquals(findWork.getTitle(), TEST_WORK_TITLE);
    }
    @Test
    public void removeTextBoxProjectElement(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        TextBox textBox = createTextBox(member);
        ProjectElement textBoxPE = createTextboxProjectElement(project, textBox);

        ProjectElement projectElement = projectElementService.createProjectElement(textBoxPE);

        //when
        projectElementService.removeById(projectElement.getId());

        //then
        List<ProjectElement> projectElementList = projectElementService.findProjectElementByProject(project);
        assertEquals(projectElementList.size(), 0);
        assertThrows(NoSuchElementException.class, () -> {
            textBoxService.findById(textBox.getId());
        });
    }
    @Test
    public void removeDividerProjectElement(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        ProjectElement dividerPE = createDividerProjectElement(project);

        ProjectElement projectElement = projectElementService.createProjectElement(dividerPE);

        //when
        projectElementService.removeById(projectElement.getId());

        //then
        List<ProjectElement> projectElementList = projectElementService.findProjectElementByProject(project);
        assertEquals(projectElementList.size(), 0);
    }

    private Member createMemberInTest(){
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();
        return memberService.createMember(member);
    }

    private Project createProjectInTest(Member member){
        return projectService.createProject(
                member,
                TEST_PROJECT_ORIGIN_URL,
                TEST_PROJECT_THUMBNAIL_URL,
                TEST_PROJECT_TITLE,
                null
        );
    }
    private Work createWork(Member member){
        Work work = new WorkBuilder()
                .member(member)
                .originUrl(TEST_WORK_ORIGINAL_URL)
                .thumbnailUrl(TEST_WORK_THUMBNAIL_URL)
                .title(TEST_WORK_TITLE)
                .build();

        return workService.createWork(work);
    }
    private TextBox createTextBox(Member member) {
        TextBox textBox = TextBox.createTextBox(TEST_TEXTBOX_CONTENT);

        return textBoxService.createTextBox(textBox);
    }

    private ProjectElement createWorkProjectElement(Project project, Work work){
        return new WorkInProjectBuilder()
                .project(project)
                .work(work)
                .workAlignment(TEST_WORK_ALIGNMENT)
                .build();
    }

    private ProjectElement createTextboxProjectElement(Project project, TextBox textBox){
        return new TextBoxInProjectBuilder()
                .project(project)
                .textBox(textBox)
                .textBoxAlignment(TEST_TEXTBOX_ALIGNMENT)
                .build();
    }

    private ProjectElement createDividerProjectElement(Project project){
        return new DividerInProjectBuilder()
                .project(project)
                .dividerType(TEST_DIVIDER_TYPE)
                .build();
    }
}

package com.architrave.portfolio.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.architrave.portfolio.domain.model.*;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.domain.model.builder.WorkBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.DividerInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.TextBoxInProjectBuilder;
import com.architrave.portfolio.domain.model.builder.projectElementBuilder.WorkInProjectBuilder;
import com.architrave.portfolio.domain.model.enumType.DividerType;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.model.enumType.TextBoxAlignment;
import com.architrave.portfolio.domain.model.enumType.WorkAlignment;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProjectElementServiceTest {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final WorkService workService;
    private final TextBoxService textBoxService;
    private final ProjectElementService projectElementService;
    private final EntityManager em;

    @Autowired
    public ProjectElementServiceTest(ProjectService projectService,
                                     MemberService memberService,
                                     WorkService workService,
                                     TextBoxService textBoxService,
                                     ProjectElementService projectElementService,
                                     EntityManager em) {
        this.projectService = projectService;
        this.memberService = memberService;
        this.workService = workService;
        this.textBoxService = textBoxService;
        this.projectElementService = projectElementService;
        this.em = em;
    }


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
    @Transactional
    public void getProjectElementsInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        Work work = createWork(member);
        ProjectElement workPE = createWorkProjectElement(project, work,0);
        TextBox textBox = createTextBox(member);
        ProjectElement textBoxPE = createTextboxProjectElement(project, textBox,1);
        ProjectElement dividerPE = createDividerProjectElement(project, 2);

        projectElementService.createProjectElement(workPE);
        projectElementService.createProjectElement(textBoxPE);
        projectElementService.createProjectElement(dividerPE);

        em.flush();
        em.clear();

        //when
        List<ProjectElement> projectElementList = projectElementService.findProjectElementByProject(project);
        ProjectElement projectElement_0 = projectElementList.get(0);
        ProjectElement projectElement_1 = projectElementList.get(1);
        ProjectElement projectElement_2 = projectElementList.get(2);

        //then
        assertEquals(projectElementList.size(), 3);
        assertNotNull(projectElement_0.getWork());
        assertNotNull(projectElement_1.getTextBox());
        assertNotNull(projectElement_2.getDividerType());
    }

    @Test
    @Transactional
    public void addWorkInProject(){
        //given
        Member member = createMemberInTest();
        Work work = createWork(member);
        Project project = createProjectInTest(member);
        ProjectElement workPE = createWorkProjectElement(project, work,0);

        //when
        projectElementService.createProjectElement(workPE);
        em.flush();
        em.clear();

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
    @Transactional
    public void addTextBoxInProject(){
        //given
        Member member = createMemberInTest();
        TextBox textBox = createTextBox(member);
        Project project = createProjectInTest(member);
        ProjectElement textBoxPE = createTextboxProjectElement(project, textBox,0);

        //when
        projectElementService.createProjectElement(textBoxPE);
        em.flush();
        em.clear();

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
    @Transactional
    public void addDividerInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        ProjectElement dividerPE = createDividerProjectElement(project, 0);

        //when
        projectElementService.createProjectElement(dividerPE);
        em.flush();
        em.clear();

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
    @Transactional
    public void updateWorkInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        Work work = createWork(member);
        ProjectElement workPE = createWorkProjectElement(project, work,0);
        projectElementService.createProjectElement(workPE);

        em.flush();
        em.clear();

        //when
        Work findWork = workService.findWorkById(1L);
        findWork.setTitle(TEST_WORK_TITLE_CHANGE);

        projectElementService.updateProjectElementWork(
                work,
                1L,
                TEST_WORK_ALIGNMENT_CHANGE,
                null
        );
        em.flush();
        em.clear();

        //then
        ProjectElement findWorkPE2 = projectElementService.findById(1L);
        assertEquals(findWorkPE2.getWork().getTitle(), TEST_WORK_TITLE_CHANGE);
        assertEquals(findWorkPE2.getWorkAlignment(), TEST_WORK_ALIGNMENT_CHANGE);
        assertEquals(findWorkPE2.getPeOrder(), workPE.getPeOrder());
        assertTrue(findWorkPE2.getProject().getProjectElementList().contains(findWorkPE2));
    }

    @Test
    @Transactional
    public void updateTextBoxInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        TextBox textBox = createTextBox(member);
        ProjectElement textBoxPE = createTextboxProjectElement(project, textBox,1);

        projectElementService.createProjectElement(textBoxPE);
        em.flush();
        em.clear();

        //when
        TextBox findTextBox = textBoxService.findById(1L);
        findTextBox.setContent(TEST_TEXTBOX_CONTENT_CHANGED);

        projectElementService.updateProjectElementTextBox(
                findTextBox,
                1L,
                TEST_TEXTBOX_ALIGNMENT_CHANGE,
                null
        );
        em.flush();
        em.clear();

        //then
        ProjectElement findTextBoxPE = projectElementService.findById(1L);
        assertNull(findTextBoxPE.getWork());
        assertEquals(findTextBoxPE.getTextBox().getContent(), TEST_TEXTBOX_CONTENT_CHANGED);
        assertEquals(findTextBoxPE.getTextBoxAlignment(), TEST_TEXTBOX_ALIGNMENT_CHANGE);
        assertEquals(findTextBoxPE.getPeOrder(), 1);
        assertTrue(findTextBoxPE.getProject().getProjectElementList().contains(findTextBoxPE));
    }

    @Test
    @Transactional
//    @Commit
    public void updateDividerInProject(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        ProjectElement dividerPE = createDividerProjectElement(project, 0);

        projectElementService.createProjectElement(dividerPE);

        em.flush();
        em.clear();

        //when
        projectElementService.updateProjectElementDivider(
                1L,
                null,
                1
        );
        em.flush();
        em.clear();

        //then
        ProjectElement findDividerPE = projectElementService.findById(1L);
        assertNull(findDividerPE.getWork());
        assertNull(findDividerPE.getTextBox());
        assertEquals(findDividerPE.getPeOrder(), 1);
        assertTrue(findDividerPE.getProject().getProjectElementList().contains(findDividerPE));
    }

    @Test
    @Transactional
    public void removeProjectElement(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        ProjectElement dividerPE = createDividerProjectElement(project, 0);

        ProjectElement projectElement = projectElementService.createProjectElement(dividerPE);
        em.flush();
        em.clear();

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
        Member member1 = memberService.createMember(member);
        System.out.println("hello from createMemberInTest: "  + member1.getId());
        return member1;
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
                .originImgUrl(TEST_WORK_ORIGINAL_URL)
                .thumbnailUrl(TEST_WORK_THUMBNAIL_URL)
                .title(TEST_WORK_TITLE)
                .build();

        return workService.createWork(work);
    }
    private TextBox createTextBox(Member member) {
        TextBox textBox = TextBox.createTextBox(TEST_TEXTBOX_CONTENT);

        return textBoxService.createTextBox(textBox);
    }

    private ProjectElement createWorkProjectElement(Project project, Work work, Integer peOrder){
        return new WorkInProjectBuilder()
                .project(project)
                .work(work)
                .workAlignment(TEST_WORK_ALIGNMENT)
                .peOrder(peOrder)
                .build();
    }

    private ProjectElement createTextboxProjectElement(Project project, TextBox textBox, Integer peOrder){
        return new TextBoxInProjectBuilder()
                .project(project)
                .textBox(textBox)
                .textBoxAlignment(TEST_TEXTBOX_ALIGNMENT)
                .peOrder(peOrder)
                .build();
    }

    private ProjectElement createDividerProjectElement(Project project, Integer peOrder){
        return new DividerInProjectBuilder()
                .project(project)
                .dividerType(TEST_DIVIDER_TYPE)
                .peOrder(peOrder)
                .build();
    }
}

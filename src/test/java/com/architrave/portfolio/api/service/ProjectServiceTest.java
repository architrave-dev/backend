package com.architrave.portfolio.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootTest
public class ProjectServiceTest {

    private final ProjectService projectService;
    private final MemberService memberService;
    private final ProjectInfoService projectInfoService;
    private final EntityManager em;

    @Autowired
    public ProjectServiceTest(ProjectService projectService,
                              MemberService memberService,
                              ProjectInfoService projectInfoService,
                              EntityManager em) {
        this.projectService = projectService;
        this.memberService = memberService;
        this.projectInfoService = projectInfoService;
        this.em = em;
    }

    private final String TEST_MEMBER_EMAIL = "lee@gmail.com";
    private final String TEST_MEMBER_PASSWORD = "12345";
    private final String TEST_MEMBER_USERNAME = "이중섭";
    private final RoleType ROLE_USER = RoleType.USER;

    private final String TEST_PROJECT_TITLE = "test project";

    private final String TEST_PROJECTINFO_NAME = "test";
    private final String TEST_PROJECTINFO_VALUE = "test value";
    private final String TEST_PROJECTINFO_NAME_2 = "test2";
    private final String TEST_PROJECTINFO_VALUE_2 = "test2 value";

    @Test
    @Transactional
    public void getProjectListByMember(){
        //given
        Member member = createMemberInTest();
        createProjectInTest(member);
        createProjectInTest(member);
        em.flush();
        em.clear();

        //when
        List<Project> projectList = projectService.findByMember(member);

        //then
        assertEquals(projectList.size(), 2);
        assertNotEquals(projectList.get(0).getId(), projectList.get(1).getId());
        for(Project each: projectList){
            assertEquals(each.getMember().getAui(), member.getAui());
            assertEquals(each.getTitle(), TEST_PROJECT_TITLE);
        }
    }

    @Test
    @Transactional
    public void getProjectListEmpty(){
        //given
        Member member = createMemberInTest();
        em.flush();
        em.clear();

        //when
        List<Project> projectList = projectService.findByMember(member);

        //then
        assertEquals(projectList.size(), 0);
    }

    @Test
    @Transactional
    public void getProjectDetail(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);
        em.flush();
        em.clear();

        //when
        Project findProject = projectService.findById(project.getId());

        //then
        assertEquals(findProject.getTitle(), TEST_PROJECT_TITLE);
        assertEquals(findProject.getMember().getAui(), member.getAui());
    }
    @Test
    @Transactional(readOnly = true)
    public void getProjectDetailEmpty(){
        //given
        //when then
        assertThrows(NoSuchElementException.class, () -> {
            projectService.findById(2L);
        });
    }

    @Test
    @Transactional
    public void createProject(){
        //given
        Member member = createMemberInTest();

        //when
        Project createdProject = createProjectInTest(member);
        em.flush();
        em.clear();

        //then
        Project findProject = projectService.findById(createdProject.getId());
        assertEquals(findProject.getMember().getAui(), member.getAui());
        assertEquals(findProject.getTitle(), TEST_PROJECT_TITLE);
    }


    @Test
    @Transactional
    public void updateProject(){
        //given
        Member member = createMemberInTest();
        Project createdProject = createProjectInTest(member);
        em.flush();
        em.clear();

        //when
        Project findProject = projectService.findById(createdProject.getId());
        findProject.setIsDeleted(true);
        em.flush();
        em.clear();

        //then
        Project findProject2 = projectService.findById(createdProject.getId());
        assertTrue(findProject2.getIsDeleted());
        assertEquals(findProject2.getTitle(), findProject.getTitle());
    }

    @Test
    @Transactional
    public void addProjectInfoInProject(){
        //given
        Member member = createMemberInTest();
        Project createdProject = createProjectInTest(member);
        em.flush();
        em.clear();

        //when
        Project findProject = projectService.findById(createdProject.getId());
        List<ProjectInfo> projectInfoByProject = projectInfoService.findProjectInfoByProject(findProject);
        assertEquals(projectInfoByProject.size(), 0);
        projectInfoService.createProjectInfo(findProject, TEST_PROJECTINFO_NAME, TEST_PROJECTINFO_VALUE);

        em.flush();
        em.clear();

        //then
        Project findProject2 = projectService.findById(createdProject.getId());
        List<ProjectInfo> projectInfoByProject2 = projectInfoService.findProjectInfoByProject(findProject2);
        assertEquals(projectInfoByProject2.size(), 1);
    }

    @Test
    @Transactional
    public void updateProjectInfoInProject(){
        //given
        Member member = createMemberInTest();
        Project createdProject = createProjectInTest(member);
        projectInfoService.createProjectInfo(createdProject, TEST_PROJECTINFO_NAME, TEST_PROJECTINFO_VALUE);
        projectInfoService.createProjectInfo(createdProject, TEST_PROJECTINFO_NAME_2, TEST_PROJECTINFO_VALUE_2);
        em.flush();
        em.clear();

        //when
        List<ProjectInfo> projectInfoByProject = projectInfoService.findProjectInfoByProject(createdProject);
        assertEquals(projectInfoByProject.size(), 2);

        ProjectInfo projectInfo = projectInfoByProject.get(0);
        projectInfoService.updateProjectInfo(
                projectInfo.getId(),
                projectInfo.getCustomName(),
                TEST_PROJECTINFO_VALUE_2);
        em.flush();
        em.clear();

        //then
        List<ProjectInfo> projectInfoByProject2 = projectInfoService.findProjectInfoByProject(createdProject);
        ProjectInfo projectInfo2 = projectInfoByProject2.get(0);

        assertEquals(projectInfoByProject.size(), 2);
        assertEquals(projectInfo2.getCustomValue(), TEST_PROJECTINFO_VALUE_2);
    }

    @Test
    @Transactional
    public void deleteProjectInfoInProject(){
        //given
        Member member = createMemberInTest();
        Project createdProject = createProjectInTest(member);
        projectInfoService.createProjectInfo(createdProject, TEST_PROJECTINFO_NAME, TEST_PROJECTINFO_VALUE);
        projectInfoService.createProjectInfo(createdProject, TEST_PROJECTINFO_NAME_2, TEST_PROJECTINFO_VALUE_2);
        em.flush();
        em.clear();

        //when
        List<ProjectInfo> projectInfoByProject = projectInfoService.findProjectInfoByProject(createdProject);
        assertEquals(projectInfoByProject.size(), 2);
        ProjectInfo projectInfo = projectInfoByProject.get(0);
        projectInfoService.removeProjectInfo(projectInfo.getId());
        em.flush();
        em.clear();

        //then
        List<ProjectInfo> projectInfoByProject2 = projectInfoService.findProjectInfoByProject(createdProject);
        assertEquals(projectInfoByProject2.size(), 1);
    }

    @Test
    public void deleteProject(){
        //Project를 테이블에서 제거하는 로직은 존재하지 않는다.
        //isDeleted를 true로 update하는 로직으로 대체한다.
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
        Project project = new ProjectBuilder()
                .member(member)
                .title(TEST_PROJECT_TITLE)
                .build();
        //when
        return projectService.createProject(project);
    }

}

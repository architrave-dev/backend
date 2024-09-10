package com.architrave.portfolio.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
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
public class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ProjectInfoService projectInfoService;

    private final String TEST_MEMBER_EMAIL = "lee@gmail.com";
    private final String TEST_MEMBER_PASSWORD = "12345";
    private final String TEST_MEMBER_USERNAME = "이중섭";
    private final RoleType ROLE_USER = RoleType.USER;

    private final String TEST_PROJECT_TITLE = "test project";

    private final String TEST_PROJECTINFO_NAME = "test";
    private final String TEST_PROJECTINFO_VALUE = "test value";
    private final String TEST_PROJECTINFO_NAME_2 = "test2";
    private final String TEST_PROJECTINFO_VALUE_2 = "test2 value";

    private final String TEST_PROJECT_ORIGIN_URL = "test origin url";
    private final String TEST_PROJECT_THUMBNAIL_URL = "test thumbnail url";


    @Test
    public void getProjectListByMember(){
        //given
        Member member = createMemberInTest();
        createProjectInTest(member);
        createProjectInTest(member);

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
    public void getProjectListEmpty(){
        //given
        Member member = createMemberInTest();

        //when
        List<Project> projectList = projectService.findByMember(member);

        //then
        assertEquals(projectList.size(), 0);
    }

    @Test
    public void getProjectDetail(){
        //given
        Member member = createMemberInTest();
        Project project = createProjectInTest(member);

        //when
        Project findProject = projectService.findById(project.getId());

        //then
        assertEquals(findProject.getTitle(), TEST_PROJECT_TITLE);
        assertEquals(findProject.getMember().getAui(), member.getAui());
    }
    @Test
    public void getProjectDetailEmpty(){
        //given
        //when then
        assertThrows(NoSuchElementException.class, () -> {
            projectService.findById(2L);
        });
    }

    @Test
    public void createProject(){
        //given
        Member member = createMemberInTest();

        //when
        Project createdProject = createProjectInTest(member);

        //then
        Project findProject = projectService.findById(createdProject.getId());
        assertEquals(findProject.getMember().getAui(), member.getAui());
        assertEquals(findProject.getTitle(), TEST_PROJECT_TITLE);
    }


    @Test
    public void updateProject(){
        //given
        Member member = createMemberInTest();
        Project createdProject = createProjectInTest(member);

        //when
        Project updatedProject = projectService.updateProject(
                createdProject.getId(),
                null,
                null,
                null,
                null
        );

        //then
        Project findProject = projectService.findById(createdProject.getId());
        assertEquals(findProject.getTitle(), createdProject.getTitle());

    }

    @Test
    public void addProjectInfoInProject(){
        //given
        Member member = createMemberInTest();
        Project createdProject = createProjectInTest(member);

        //when
        Project findProject = projectService.findById(createdProject.getId());
        List<ProjectInfo> projectInfoByProject = projectInfoService.findProjectInfoByProject(findProject);
        assertEquals(projectInfoByProject.size(), 0);
        projectInfoService.createProjectInfo(findProject, TEST_PROJECTINFO_NAME, TEST_PROJECTINFO_VALUE);


        //then
        Project findProject2 = projectService.findById(createdProject.getId());
        List<ProjectInfo> projectInfoByProject2 = projectInfoService.findProjectInfoByProject(findProject2);
        assertEquals(projectInfoByProject2.size(), 1);
    }

    @Test
    public void updateProjectInfoInProject(){
        //given
        Member member = createMemberInTest();
        Project createdProject = createProjectInTest(member);
        projectInfoService.createProjectInfo(createdProject, TEST_PROJECTINFO_NAME, TEST_PROJECTINFO_VALUE);
        projectInfoService.createProjectInfo(createdProject, TEST_PROJECTINFO_NAME_2, TEST_PROJECTINFO_VALUE_2);

        //when
        List<ProjectInfo> projectInfoByProject = projectInfoService.findProjectInfoByProject(createdProject);
        assertEquals(projectInfoByProject.size(), 2);

        ProjectInfo projectInfo = projectInfoByProject.get(0);
        projectInfoService.updateProjectInfo(
                projectInfo.getId(),
                projectInfo.getCustomName(),
                TEST_PROJECTINFO_VALUE_2);

        //then
        List<ProjectInfo> projectInfoByProject2 = projectInfoService.findProjectInfoByProject(createdProject);
        ProjectInfo projectInfo2 = projectInfoByProject2.get(0);

        assertEquals(projectInfoByProject.size(), 2);
        assertEquals(projectInfo2.getCustomValue(), TEST_PROJECTINFO_VALUE_2);
    }

    @Test
    public void deleteProjectInfoInProject(){
        //given
        Member member = createMemberInTest();
        Project createdProject = createProjectInTest(member);
        projectInfoService.createProjectInfo(createdProject, TEST_PROJECTINFO_NAME, TEST_PROJECTINFO_VALUE);
        projectInfoService.createProjectInfo(createdProject, TEST_PROJECTINFO_NAME_2, TEST_PROJECTINFO_VALUE_2);

        //when
        List<ProjectInfo> projectInfoByProject = projectInfoService.findProjectInfoByProject(createdProject);
        assertEquals(projectInfoByProject.size(), 2);
        ProjectInfo projectInfo = projectInfoByProject.get(0);
        projectInfoService.removeProjectInfo(projectInfo.getId());

        //then
        List<ProjectInfo> projectInfoByProject2 = projectInfoService.findProjectInfoByProject(createdProject);
        assertEquals(projectInfoByProject2.size(), 1);
    }

    @Test
    public void deleteProject(){
        //Project를 테이블에서 제거하는 로직은 존재하지 않는다.
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
        //when
        return projectService.createProject(
                member,
                TEST_PROJECT_ORIGIN_URL,
                TEST_PROJECT_THUMBNAIL_URL,
                TEST_PROJECT_TITLE,
                null
        );
    }

}

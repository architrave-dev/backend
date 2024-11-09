package com.architrave.portfolio.project.projectInfo;

import com.architrave.portfolio.api.service.ProjectInfoService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.MemberRepository;
import com.architrave.portfolio.domain.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProjectInfoIntegrationTest {


    @Autowired
    private ProjectInfoService projectInfoService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;
    private Project testProject;

    @BeforeEach
    void setUp(){
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();
        memberRepository.save(testMember);
        testProject = new ProjectBuilder()
                .member(testMember)
                .title("test title")
                .originUrl("test origin")
                .thumbnailUrl("test thumbnail")
                .build();
        projectRepository.save(testProject);
    }

    @Test
    @DisplayName("create update delete projectInfo")
    void projectInfoLifecycle(){
        //Create
        ProjectInfo created = projectInfoService.createProjectInfo(
                testProject,
                "test name",
                "test value"
        );
        // verify
        assertThat(created).isNotNull();
        assertThat(created.getCustomName()).isEqualTo("test name");
        assertThat(created.getProject().getTitle()).isEqualTo("test title");

        //Update
        ProjectInfo updated = projectInfoService.updateProjectInfo(
                created.getId(),
                "updated name",
                "updated value"
        );
        // verify
        assertThat(updated).isNotNull();
        assertThat(updated.getCustomName()).isEqualTo("updated name");
        assertThat(updated.getCustomValue()).isEqualTo("updated value");
        assertThat(updated.getProject().getTitle()).isEqualTo("test title");

        //Delete
        projectInfoService.removeProjectInfoById(updated.getId());

        // verify
        List<ProjectInfo> remains = projectInfoService.findProjectInfoByProject(testProject);
        assertThat(remains).isEmpty();
    }

    @Test
    @DisplayName("multiple projectInfo")
    void multipleProjectInfoCreateAndRemove (){
        //Create 2 projectInfo
        projectInfoService.createProjectInfo(
                testProject,
                "test name 1",
                "test value 1"
        );
        projectInfoService.createProjectInfo(
                testProject,
                "test name 2",
                "test value 2"
        );

        //verify
        List<ProjectInfo> projectInfoList = projectInfoService.findProjectInfoByProject(testProject);
        assertThat(projectInfoList).hasSize(2);

        //Remove all
        projectInfoService.removeProjectInfoByProject(testProject);

        //verify
        List<ProjectInfo> remains = projectInfoService.findProjectInfoByProject(testProject);
        assertThat(remains).hasSize(0);
    }
}

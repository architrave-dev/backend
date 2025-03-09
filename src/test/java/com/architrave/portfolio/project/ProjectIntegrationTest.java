package com.architrave.portfolio.project;

import com.architrave.portfolio.api.service.ProjectService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.MemberRepository;
import com.architrave.portfolio.domain.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProjectIntegrationTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();
        memberRepository.save(testMember);
    }

    @Test
    @DisplayName("Create, Update, Delete Project")
     void projectLifecycle() {
        // Create
        Project createdProject = projectService.createProject(
                testMember,
                "test origin",
                "test title",
                "test description",
                0
        );

        // Verify creation
        assertThat(createdProject).isNotNull();
        assertThat(createdProject.getTitle()).isEqualTo("test title");
        assertThat(createdProject.getMember().getUsername()).isEqualTo("testArtist");

        // Update
        Project updatedProject = projectService.updateProject(
                createdProject.getId(),
                "updated origin",
                "updated title",
                "updated description"
        );

        // Verify update
        assertThat(updatedProject).isNotNull();
        assertThat(updatedProject.getTitle()).isEqualTo("updated title");
        assertThat(updatedProject.getUploadFile().getOriginUrl()).isEqualTo("updated origin");

        // Delete
        projectService.removeProject(updatedProject.getId());

        // Verify deletion
        List<Project> remains = projectRepository.findByMember(testMember);
        assertThat(remains).isEmpty();
    }

    @Test
    @DisplayName("Multiple Projects Creation and Removal")
    void multipleProjectsCreateAndRemove() {
        // Create 2 projects
        projectService.createProject(
                testMember,
                "test origin 1",
                "test title 1",
                "test description 1",
                0
        );
        projectService.createProject(
                testMember,
                "test origin 2",
                "test title 2",
                "test description 2",
                1
        );

        // Verify multiple creations
        List<Project> projectList = projectRepository.findByMember(testMember);
        assertThat(projectList).hasSize(2);
    }

}

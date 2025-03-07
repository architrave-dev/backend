package com.architrave.portfolio.project;

import com.architrave.portfolio.api.service.ProjectService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectUnitTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Member testMember;
    private Project testProject;

    @BeforeEach
    void setUp() {
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();

        testProject = new ProjectBuilder()
                .member(testMember)
                .title("Test Title")
                .originUrl("test origin")
                .description("Test Description")
                .index(0)
                .build();
    }
    @Test
    @DisplayName("createProject should save and return Project")
    void createProject_ShouldSaveAndReturnProject() {
        // Arrange
        when(projectRepository.save(any(Project.class)))
                .thenReturn(testProject);

        // Act
        Project result = projectService.createProject(
                testMember,
                "test origin",
                "Test Title",
                "Test Description",
                0
        );

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(testProject);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("findById should return Project if exists")
    void findById_ShouldReturnProjectIfExists() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(testProject));

        // Act
        Project result = projectService.findById(projectId);

        // Assert
        assertThat(result).isEqualTo(testProject);
        verify(projectRepository).findById(projectId);
    }
    @Test
    @DisplayName("findById should throw exception if Project not found")
    void findById_ShouldThrowExceptionIfProjectNotFound() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findById(projectId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> projectService.findById(projectId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("there is no project that id" + projectId);
    }
    @Test
    @DisplayName("findByMember should return list of Projects")
    void findByMember_ShouldReturnListOfProjects() {
        // Arrange
        when(projectRepository.findByMember(testMember))
                .thenReturn(Arrays.asList(testProject));

        // Act
        List<Project> result = projectService.findByMember(testMember);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testProject);
        verify(projectRepository).findByMember(testMember);
    }

    @Test
    @DisplayName("updateProject should update Project details")
    void updateProject_ShouldUpdateProjectDetails() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(testProject));

        // Act
        Project updatedProject = projectService.updateProject(
                projectId,
                "new origin",
                "New Title",
                "New Description"
        );

        // Assert
        assertThat(updatedProject.getUploadFile().getOriginUrl()).isEqualTo("new origin");
        assertThat(updatedProject.getTitle()).isEqualTo("New Title");
        assertThat(updatedProject.getDescription()).isEqualTo("New Description");
        verify(projectRepository).findById(projectId);
    }

    @Test
    @DisplayName("removeProject by ID should delete Project")
    void removeProjectById_ShouldDeleteProject() {
        // Arrange
        Long projectId = 1L;
        when(projectRepository.findById(projectId))
                .thenReturn(Optional.of(testProject));

        // Act
        projectService.removeProject(projectId);

        // Assert
        verify(projectRepository).findById(projectId);
        verify(projectRepository).delete(testProject);
    }
}

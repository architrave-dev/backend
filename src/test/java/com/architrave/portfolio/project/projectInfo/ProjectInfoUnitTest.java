package com.architrave.portfolio.project.projectInfo;

import com.architrave.portfolio.api.service.ProjectInfoService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.ProjectInfo;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.ProjectBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.ProjectInfoRepository;
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
public class ProjectInfoUnitTest {

    @Mock
    private ProjectInfoRepository projectInfoRepository;

    @InjectMocks
    private ProjectInfoService projectInfoService;

    private Member testMember;
    private Project testProject;
    private ProjectInfo testProjectInfo;

    @BeforeEach
    void setUp(){
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();
        testProject = new ProjectBuilder()
                .member(testMember)
                .title("test title")
                .originUrl("test origin")
                .thumbnailUrl("test thumbnail")
                .build();
        testProjectInfo = ProjectInfo.createProjectInfo(
                testProject,
                "test name",
                "test value"
        );
    }

    @Test
    @DisplayName("findProjectInfoByProject should return List of ProjectInfo")
    void findProjectInfoByProject_ShouldReturnList (){
        // Arrange
        when(projectInfoRepository.findByProject(testProject))
                .thenReturn(Arrays.asList(testProjectInfo));

        // Act
        List<ProjectInfo> result = projectInfoService.findProjectInfoByProject(testProject);

        // Assert & 호출여부 확인
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testProjectInfo);
        verify(projectInfoRepository).findByProject(testProject);
    }

    @Test
    @DisplayName("findProjectInfoById should throw exception")
    void findProjectInfoById_ShouldThrowException (){
        // Arrange
        Long id = 1L;
        when(projectInfoRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> projectInfoService.findProjectInfoById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("no");
    }

    @Test
    @DisplayName("createProjectInfo should save ProjectInfo")
    void createProjectInfo_ShouldSavePI (){
        // Arrange
        when(projectInfoRepository.save(any(ProjectInfo.class)))
                .thenReturn(testProjectInfo);

        // Act
        ProjectInfo result = projectInfoService.createProjectInfo(
                testProjectInfo.getProject(),
                testProjectInfo.getCustomName(),
                testProjectInfo.getCustomValue());

        // Assert & 호출여부 확인
        assertThat(result).isNotNull();
        verify(projectInfoRepository).save(any(ProjectInfo.class));
    }

    @Test
    @DisplayName("updateProjectInfo should update ProjectInfo")
    void updateProjectInfo_ShouldUpdatePI (){
        // Arrange
        Long id = 1L;
        when(projectInfoRepository.findById(id))
                .thenReturn(Optional.of(testProjectInfo));

        // Act
        ProjectInfo updated = projectInfoService.updateProjectInfo(id,
                "updated name",
                "updated value"
        );

        // Assert & 호출여부 확인
        assertThat(updated).isNotNull();
        assertThat(updated.getCustomName()).isEqualTo("updated name");
        assertThat(updated.getCustomValue()).isEqualTo("updated value");
        verify(projectInfoRepository).findById(id);
    }

    @Test
    @DisplayName("removeProjectInfoById should remove ProjectInfo")
    void removeProjectInfoById_ShouldRemovePI (){
        // Arrange
        Long id = 1L;
        when(projectInfoRepository.findById(id))
                .thenReturn(Optional.of(testProjectInfo));

        // Act
        projectInfoService.removeProjectInfoById(id);

        // Assert & 호출여부 확인
        verify(projectInfoRepository).findById(id);
    }

//    @Test
//    @DisplayName("removeProjectInfoByProject should remove all related ProjectInfo")
//    void removeProjectInfoByProject_ShouldRemoveAllRelatedPI (){
//        // Arrange
//        Long id = 1L;
//        when(projectInfoRepository.findById(id))
//                .thenReturn(Optional.of(testProjectInfo));
//
//        // Act
//        projectInfoService.removeProjectInfoByProject(testProject);
//
//        // Assert & 호출여부 확인
//        verify(projectInfoRepository).findById(id);
//    }
}

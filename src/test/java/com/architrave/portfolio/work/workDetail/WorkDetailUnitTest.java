package com.architrave.portfolio.work.workDetail;

import com.architrave.portfolio.api.service.WorkDetailService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.WorkBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import com.architrave.portfolio.domain.repository.WorkDetailRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkDetailUnitTest {

    @Mock
    private WorkDetailRepository workDetailRepository;
    @InjectMocks
    private WorkDetailService workDetailService;

    private Member testMember;
    private Work testWork;
    private WorkDetail testWorkDetail;

    @BeforeEach
    void setUp() {
        // Create test data without any database interaction
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();

        testWork = new WorkBuilder()
                .member(testMember)
                .workType(WorkType.DIGITAL)
                .title("Test Artwork")
                .originUrl("work-origin.jpg")
                .build();

        UploadFile detailUploadFile = UploadFile.builder()
                .originUrl("detail-origin.jpg")
                .build();

        // Create test work detail
        testWorkDetail = WorkDetail.createWorkDetail(testWork, detailUploadFile, "Test Detail Description");
    }

    @Test
    @DisplayName("findWorkDetailByWork should return list of work details")
    void findWorkDetailByWork_ShouldReturnList() {
        // Arrange - Mock repository behavior
        when(workDetailRepository.findByWork(testWork))
                .thenReturn(Arrays.asList(testWorkDetail));

        // Act
        List<WorkDetail> result = workDetailService.findWorkDetailByWork(testWork);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testWorkDetail);
        // 호출 여부를 확인
        verify(workDetailRepository).findByWork(testWork);
    }

    @Test
    @DisplayName("findWorkDetailById should throw exception when not found")
    void findWorkDetailById_ShouldThrowException() {
        // Arrange
        Long id = 1L;
        when(workDetailRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> workDetailService.findWorkDetailById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("no");
    }

    @Test
    @DisplayName("createWorkDetail should save work detail")
    void createWorkDetail_ShouldSaveWorkDetail() {
        // Arrange
        when(workDetailRepository.save(any(WorkDetail.class))).thenReturn(testWorkDetail);

        // Act
        WorkDetail result = workDetailService.createWorkDetail(
                testWork,
                "new-origin.jpg",
                "New Description"
        );

        // Assert
        assertThat(result).isNotNull();
        verify(workDetailRepository).save(any(WorkDetail.class));
    }

    @Test
    @DisplayName("updateWorkDetail should update existing work detail")
    void updateWorkDetail_ShouldUpdateWorkDetail() {
        // Arrange
        Long id = 1L;
        when(workDetailRepository.findById(id)).thenReturn(Optional.of(testWorkDetail));

        // Act
        WorkDetail result = workDetailService.updateWorkDetail(
                id,
                "updated-origin.jpg",
                "Updated Description"
        );

        // Assert
        assertThat(result.getDescription()).isEqualTo("Updated Description");
        assertThat(result.getUploadFile().getOriginUrl()).isEqualTo("updated-origin.jpg");
        verify(workDetailRepository).findById(id);
    }

    @Test
    @DisplayName("removeWorkDetailById should delete work detail and uploadFile(cascade.All)")
    void removeWorkDetailById_ShouldDeleteWorkDetailAndUploadFile() {
        // Arrange
        Long id = 1L;
        when(workDetailRepository.findById(id)).thenReturn(Optional.of(testWorkDetail));

        // Act
        workDetailService.removeWorkDetailById(id);

        // Assert
        verify(workDetailRepository).delete(testWorkDetail);
    }
}

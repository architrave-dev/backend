package com.architrave.portfolio.work;

import com.architrave.portfolio.api.service.WorkService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.WorkBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import com.architrave.portfolio.domain.repository.WorkRepository;
import org.junit.jupiter.api.*;
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
public class WorkUnitTest {

    @Mock
    private WorkRepository workRepository;
    @InjectMocks
    private WorkService workService;

    private Member testMember;
    private Work testWork;

    @BeforeEach
    void setUp(){
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();

        testWork = new WorkBuilder()
                .member(testMember)
                .title("Test Artwork")
                .originUrl("work-origin.jpg")
                .thumbnailUrl("work-thumbnail.jpg")
                .build();
    }

    @Test
    @DisplayName("findWorkByMember should return list of work")
    void findWorkByMember_ShouldReturnList(){
        // Arrange - Mock repository behavior
        when(workRepository.findByMember(testMember))
                .thenReturn(Arrays.asList(testWork));

        //Act
        List<Work> result = workService.findWorkByMember(testMember);

        //Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testWork);
        //호출여부 확인
        verify(workRepository).findByMember(testMember);
    }

    @Test
    @DisplayName("findWorkById should throw exception when not found")
    void findWorkById_ShouldThrowException(){
        //Arrange
        Long id = 1L;
        when(workRepository.findById(id))
                .thenReturn(Optional.empty());

        //Act & Assert
        assertThatThrownBy(() -> workService.findWorkById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("there is no work that id:" + id);
    }

    @Test
    @DisplayName("createWork should save work")
    void createWork_ShouldSaveWork(){
        //Arrange
        when(workRepository.save(any(Work.class)))
                .thenReturn(testWork);

        //Act
        Work result = workService.createWork(testWork);

        //Assert
        assertThat(result).isNotNull();
        //호출 확인
        verify(workRepository).save(any(Work.class));
    }

    @Test
    @DisplayName("updateWork should update work")
    void updateWork_ShouldUpdateWork(){
        //Arrange
        Long id = 1L;
        when(workRepository.findById(id))
                .thenReturn(Optional.of(testWork));

        //Act
        Work result = workService.updateWork(id,
                WorkType.painting_water,
                "updated-origin.jpg",
                "updated-thumbnail.jpg",
                "updated-title",
                null,
                null,
                null,
                null,
                null,
                null
        );

        //Assert
        assertThat(result.getTitle()).isEqualTo("updated-title");
        assertThat(result.getUploadFile().getOriginUrl()).isEqualTo("updated-origin.jpg");
        //호출 확인
        verify(workRepository).findById(id);
    }

    @Test
    @DisplayName("removeWorkById should delete work and uploadFile(cascade.All)")
    void removeWorkById_ShouldDeleteWorkAndUploadFile(){
        //Arrange
        Long id = 1L;
        when(workRepository.findById(id))
                .thenReturn(Optional.of(testWork));

        //Act
        workService.removeWorkById(id);

        //호출 확인
        verify(workRepository).delete(testWork);
    }
}

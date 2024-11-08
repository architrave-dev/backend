package com.architrave.portfolio.career;

import com.architrave.portfolio.api.service.CareerService;
import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.CareerBuilder;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.CareerRepository;
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
public class CareerUnitTest {

    @Mock
    private CareerRepository careerRepository;
    @InjectMocks
    private CareerService careerService;

    private Member testMember;
    private Career testCareer;

    @BeforeEach
    void setUp(){
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();
        testCareer = new CareerBuilder()
                .member(testMember)
                .careerType(CareerType.COL)
                .content("test career")
                .yearFrom(2020)
                .build();
    }

    @Test
    @DisplayName("findCareerById should return list of work")
    void findCareerByMember_ShouldReturnList(){
        // Arange
        when(careerRepository.findByMember(testMember))
                .thenReturn(Arrays.asList(testCareer));
        // Act
        List<Career> result = careerService.findCareerByMember(testMember);

        // Assert & 호출여부 확인
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testCareer);
        verify(careerRepository).findByMember(testMember);
    }

    @Test
    @DisplayName("findCareerById should return Exception")
    void findCareerById_ShouldThrowException(){
        // Arange
        Long id = 1L;
        when(careerRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> careerService.findCareerById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("there is no career that id" + id);
    }

    @Test
    @DisplayName("createCareer should save career")
    void createCareer_ShouldSaveCareer(){
        // Arange
        when(careerRepository.save(any(Career.class)))
                .thenReturn(testCareer);

        // Act
        Career result = careerService.createCareer(testCareer);

        // Assert & 호출여부 확인
        assertThat(result).isNotNull();
        verify(careerRepository).save(any(Career.class));
    }

    @Test
    @DisplayName("updateCareer should update career")
    void updateCareer_ShouldUpdateCareer(){
        // Arange
        Long id = 1L;
        when(careerRepository.findById(id))
                .thenReturn(Optional.of(testCareer));

        // Act
        Career result = careerService.updateCareer(id,
                "updated career",
                2021
        );

        // Assert & 호출여부 확인
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("updated career");
        assertThat(result.getYearFrom()).isEqualTo(2021);
        verify(careerRepository).findById(id);
    }

    @Test
    @DisplayName("removeCareerById should delete career")
    void removeCareerById_ShouldDeleteCareer(){
        // Arange
        Long id = 1L;
        when(careerRepository.findById(id))
                .thenReturn(Optional.of(testCareer));

        // Act
        careerService.removeCareerById(id);

        // Assert & 호출여부 확인
        verify(careerRepository).findById(id);
    }
}

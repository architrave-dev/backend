package com.architrave.portfolio.billboard;

import com.architrave.portfolio.api.service.BillboardService;
import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.BillboardBuilder;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.BillboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BillboardUnitTest {

    @Mock
    private BillboardRepository billboardRepository;

    @InjectMocks
    private BillboardService billboardService;

    private Member testMember;
    private Billboard testBillboard;


    @BeforeEach
    void setUp(){
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();
        testBillboard = new BillboardBuilder()
                .member(testMember)
                .originUrl("billboard origin")
                .thumbnailUrl("billboard thumbnail")
                .title("billboard title")
                .build();
    }

    @Test
    @DisplayName("findByMember should return Billboard")
    void findByMember_ShouldReturnBilboard (){
        // Arrange
        when(billboardRepository.findByMember(testMember))
                .thenReturn(Optional.of(testBillboard));

        // Act
        Billboard result = billboardService.findByMember(testMember);

        // Assert & 호출여부 확인
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("billboard title");
        verify(billboardRepository).findByMember(testMember);
    }
    @Test
    @DisplayName("findByMember should return Billboard even if it doesn't have")
    void findByMember_ShouldReturnBilboard_EvenIfReturnNull (){
        // Arrange
        when(billboardRepository.findByMember(testMember))
                .thenReturn(Optional.empty());

        // Act
        Billboard result = billboardService.findByMember(testMember);

        // Assert & 호출여부 확인
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Billboard");
        verify(billboardRepository).findByMember(testMember);
    }

    @Test
    @DisplayName("findLbById should return Exception")
    void findLbById_ShouldThrowException (){
        // Arrange
        Long id = 1L;
        when(billboardRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> billboardService.findLbById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("there is no billboard that id: " + id);
    }

    @Test
    @DisplayName("createLb should save billboard")
    void createLb_ShouldSaveLb (){
        // Arrange
        when(billboardRepository.save(any(Billboard.class)))
                .thenReturn(testBillboard);
        // Act
        Billboard result = billboardService.createLb(testBillboard);

        // Assert & 호출여부 확인
        assertThat(result).isNotNull();
        verify(billboardRepository).save(any(Billboard.class));
    }
    @Test
    @DisplayName("updateLb should update billboard")
    void updateLb_ShouldUpdateLb (){
        // Arrange
        Long id = 1L;
        when(billboardRepository.findById(id))
                .thenReturn(Optional.of(testBillboard));

        // Act
        Billboard result = billboardService.updateLb(id,
                "updated billboard origin",
                "updated billboard thumbnail",
                "updated title",
                null,
                null
        );

        // Assert & 호출여부 확인
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("updated title");
        verify(billboardRepository).findById(id);
    }


}

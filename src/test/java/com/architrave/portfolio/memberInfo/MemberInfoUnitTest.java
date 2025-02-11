package com.architrave.portfolio.memberInfo;

import com.architrave.portfolio.api.service.MemberInfoService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.builder.MemberInfoBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.MemberInfoRepository;
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
public class MemberInfoUnitTest {

    @Mock
    private MemberInfoRepository memberInfoRepository;
    @InjectMocks
    private MemberInfoService memberInfoService;

    private Member testMember;
    private MemberInfo testMemberInfo;

    @BeforeEach
    void setUp(){
        testMember = new MemberBuilder()
                .email("test@example.com")
                .password("password123")
                .username("testArtist")
                .role(RoleType.USER)
                .build();
        testMemberInfo = new MemberInfoBuilder()
                .member(testMember)
                .originUrl("test originUrl")
                .name("test name")
                .build();
    }

    @Test
    @DisplayName("findMIByMember should return MemberInfo even if it doesn't have")
    void findMIByMember_ShouldReturnMemberInfo(){
        // Arrange
        when(memberInfoRepository.findByMember(testMember))
                .thenReturn(Optional.of(testMemberInfo));

        // Act
        MemberInfo result = memberInfoService.findMIByMember(testMember);

        // Assert & verify
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("test name");
        verify(memberInfoRepository).findByMember(testMember);
    }

    @Test
    @DisplayName("findMIById should throw exception")
    void findMIById_ShouldReturnException(){
        // Arrange
        Long id = 1L;
        when(memberInfoRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act & Assert & verify
        assertThatThrownBy(() -> memberInfoService.findMIById(id))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("there is no memberInfo that id: " + id);
    }

    @Test
    @DisplayName("createMI should save MemberInfo")
    void createMI_ShouldCreateMemberInfo(){
        // Arrange
        when(memberInfoRepository.save(any(MemberInfo.class)))
                .thenReturn(testMemberInfo);

        // Act
        MemberInfo result = memberInfoService.createMI(testMemberInfo);

        // Assert & verify
        assertThat(result.getName()).isEqualTo("test name");
        verify(memberInfoRepository).save(testMemberInfo);
    }
    @Test
    @DisplayName("updateMI should update MemberInfo")
    void updateMI_ShouldUpdateMemberInfo(){
        // Arrange
        Long id = 1L;
        when(memberInfoRepository.findById(id))
                .thenReturn(Optional.of(testMemberInfo));

        // Act
        MemberInfo updated = memberInfoService.updateMI(id,
                "updated originUrl",
                "updated title",
                null,
                null,
                null,
                null,
                null
        );

        // Assert & verify
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("updated name");
        assertThat(updated.getUploadFile().getOriginUrl()).isEqualTo("updated originUrl");
        verify(memberInfoRepository).findById(id);
    }
}

package com.architrave.portfolio.api.service;

import com.architrave.portfolio.api.dto.work.request.UpdateWorkRequestDto;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.WorkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkServiceTest {

    @Mock
    private WorkRepository workRepository;

    @InjectMocks
    private WorkService workService;

    private Member authenticatedMember;
    private Work work;

    @BeforeEach
    void setUp() {
        authenticatedMember = Member.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .aui("aui")
                .role(RoleType.USER)
                .description("description")
                .build();
        Size size = new Size();
        size.setDepth(100);
        size.setHeight(100);
        size.setWidth(100);
        work = Work.builder()
                .id(1L)
                .member(authenticatedMember)
                .title("Test Title")
                .description("Test Description")
                .size(size)
                .material("Test Material")
                .prodYear(2024)
                .build();
    }

    @Test
    void Work_수정시_성공해야한다() {
        Size updatedSize = new Size();
        updatedSize.setDepth(200);
        updatedSize.setHeight(200);
        updatedSize.setWidth(200);
        UpdateWorkRequestDto updateWorkRequestDto = UpdateWorkRequestDto.builder()
                .title("Updated Title")
                .description("Updated Description")
                .size(updatedSize)
                .material("Updated Material")
                .prodYear(2000)
                .build();

        when(workRepository.findById(work.getId())).thenReturn(Optional.of(work));

        workService.updateWork(work.getId(), authenticatedMember, updateWorkRequestDto);

        assertEquals("Updated Title", work.getTitle());
        assertEquals("Updated Description", work.getDescription());
        assertEquals(200, work.getSize().getDepth());
        assertEquals(2000, work.getProdYear());
        verify(workRepository, times(1)).findById(work.getId());
    }

    @Test
    void Work_수정시_id조회_실패로_NoSuchElementException이_발생해야한다() {
        Size updatedSize = new Size();
        updatedSize.setDepth(200);
        updatedSize.setHeight(200);
        updatedSize.setWidth(200);
        UpdateWorkRequestDto updateWorkRequestDto = UpdateWorkRequestDto.builder()
                .title("Updated Title")
                .description("Updated Description")
                .size(updatedSize)
                .material("Updated Material")
                .prodYear(2000)
                .build();
        when(workRepository.findById(work.getId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> workService.updateWork(work.getId(), authenticatedMember, updateWorkRequestDto));
        verify(workRepository, times(1)).findById(work.getId());
    }

    @Test
    void Work_수정시_권한이_없어_AccessDeniedException이_발생해야한다() {
        Size updatedSize = new Size();
        updatedSize.setDepth(200);
        updatedSize.setHeight(200);
        updatedSize.setWidth(200);

        Member otherMember = Member.builder()
                .id(2L)
                .email("other@example.com")
                .password("other password")
                .aui("otheraui")
                .role(RoleType.USER)
                .description("other description")
                .build();
        Size size = new Size();
        size.setDepth(100);
        size.setHeight(100);
        size.setWidth(100);
        work = Work.builder()
                .id(1L)
                .member(otherMember)
                .title("Test Title")
                .description("Test Description")
                .size(size)
                .material("Test Material")
                .prodYear(2024)
                .build();
        when(workRepository.findById(work.getId())).thenReturn(Optional.of(work));

        UpdateWorkRequestDto updateWorkRequestDto = UpdateWorkRequestDto.builder()
                .title("Updated Title")
                .description("Updated Description")
                .size(updatedSize)
                .material("Updated Material")
                .prodYear(2000)
                .build();

        assertThrows(AccessDeniedException.class, () -> workService.updateWork(work.getId(), authenticatedMember, updateWorkRequestDto));
        verify(workRepository, times(1)).findById(work.getId());
    }
}
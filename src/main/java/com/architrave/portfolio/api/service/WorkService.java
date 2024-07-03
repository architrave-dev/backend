package com.architrave.portfolio.api.service;

import com.architrave.portfolio.api.dto.work.request.CreateWorkRequest;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkRequestDto;
import com.architrave.portfolio.api.dto.work.response.WorkResponse;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;

    public Long createWork(CreateWorkRequest createWorkRequest, Member authenticatedMember) {
        return workRepository.save(createWorkRequest.toWork(authenticatedMember)).getId();
    }

    public WorkResponse getWork(Long workId) {
        Work work = workRepository.findById(workId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 Work Id 값 입니다."));

        return new WorkResponse(work);
    }

    public List<WorkResponse> getAllWorksByMember(Member authenticatedMember) {
        return workRepository.findAllByMember(authenticatedMember)
                .stream()
                .map(work -> WorkResponse.builder().work(work).build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateWork(Long workId, Member authenticatedMember, UpdateWorkRequestDto updateWorkRequestDto) {
        Work work = workRepository.findById(workId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 Work Id 값 입니다."));

        if (!authenticatedMember.getId().equals(work.getMember().getId())) {
            throw new AccessDeniedException("Work 수정을 위한 권한이 없습니다.");
        }

        work.update(
                updateWorkRequestDto.getTitle(),
                updateWorkRequestDto.getDescription(),
                updateWorkRequestDto.getSize(),
                updateWorkRequestDto.getDescription(),
                updateWorkRequestDto.getProdYear()
        );
    }

    @Transactional
    public void deleteWork(Long workId, Member authenticatedMember) {
        Work work = workRepository.findById(workId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 Work Id 값 입니다."));

        if (!authenticatedMember.getId().equals(work.getMember().getId())) {
            throw new AccessDeniedException("Work 삭제를 위한 권한이 없습니다.");
        }

        workRepository.deleteById(workId);
    }

}

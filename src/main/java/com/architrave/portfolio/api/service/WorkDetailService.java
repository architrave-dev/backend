package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.domain.repository.WorkDetailRepository;
import com.architrave.portfolio.global.aop.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Trace
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkDetailService {

    private final WorkDetailRepository workDetailRepository;

    @Transactional(readOnly = true)
    public List<WorkDetail> findWorkDetailByWork(Work work){
        return workDetailRepository.findByWork(work);
    }

    @Transactional(readOnly = true)
    public WorkDetail findWorkDetailById(Long workDetailId){
        return workDetailRepository.findById(workDetailId)
                .orElseThrow(() -> new NoSuchElementException("no"));
    }

    @Transactional
    public WorkDetail createWorkDetail(Work work, String originUrl, String thumbnailUrl, String description) {
        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originUrl)
                .thumbnailUrl(thumbnailUrl)
                .build();
        WorkDetail workDetail = WorkDetail.createWorkDetail(work, uploadFile, description);
        return workDetailRepository.save(workDetail);
    }

    @Transactional
    public WorkDetail updateWorkDetail(Long workDetailId, String originUrl, String thumbnailUrl,  String description){
        WorkDetail workDetail = findWorkDetailById(workDetailId);
        if(originUrl != null || thumbnailUrl != null){
            workDetail.setUploadFileUrl(originUrl, thumbnailUrl);
        }
        if(description != null) workDetail.setDescription(description);

        return workDetail;
    }

    @Transactional
    public void removeWorkDetailById(Long workDetailId){
        WorkDetail workDetail = findWorkDetailById(workDetailId);
        workDetailRepository.delete(workDetail);
    }

    @Transactional
    public void removeWorkDetailByWork(Work work){
        workDetailRepository.deleteByWork(work);
    }
}

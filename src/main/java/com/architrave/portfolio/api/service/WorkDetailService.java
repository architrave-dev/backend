package com.architrave.portfolio.api.service;

import com.architrave.portfolio.api.dto.work.response.WorkDetailSimpleDto;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.domain.repository.WorkDetailRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
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
    private final UploadFileService uploadFileService;

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
    public WorkDetail createWorkDetail(Work work, String originUrl, String description) {
        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originUrl)
                .build();
        WorkDetail workDetail = WorkDetail.createWorkDetail(work, uploadFile, description);
        return workDetailRepository.save(workDetail);
    }

    @Transactional
    public WorkDetail updateWorkDetail(Long workDetailId, String originUrl, String description){
        WorkDetail workDetail = findWorkDetailById(workDetailId);
        if (!workDetail.getUploadFile().getOriginUrl().equals(originUrl)
        ) {
            uploadFileService.deleteUploadFile(workDetail.getUploadFile());
            workDetail.setUploadFileUrl(originUrl);
        }
        if (!workDetail.getDescription().equals(description)) workDetail.setDescription(description);

        return workDetail;
    }

    @Transactional
    public void removeWorkDetailById(Long workDetailId){
        WorkDetail workDetail = findWorkDetailById(workDetailId);
        uploadFileService.deleteUploadFile(workDetail.getUploadFile());
        workDetailRepository.delete(workDetail);
    }

    @Transactional
    public void removeWorkDetailByWork(Work work){
        List<WorkDetail> workDetailByWork = findWorkDetailByWork(work);
        workDetailByWork.forEach(wd -> uploadFileService.deleteUploadFile(wd.getUploadFile()));
        workDetailRepository.deleteByWork(work);
    }

    @Transactional(readOnly = true)
    public List<WorkDetailSimpleDto> findSimpleWorkDetailByWork(Work work) {
        return workDetailRepository.findSimpleByWork(work);
    }
}

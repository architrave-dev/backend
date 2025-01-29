package com.architrave.portfolio.api.service;

import com.architrave.portfolio.api.dto.work.response.WorkSimpleDto;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.builder.WorkBuilder;
import com.architrave.portfolio.domain.model.enumType.WorkType;
import com.architrave.portfolio.domain.repository.WorkRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Trace
@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;
    private final UploadFileService uploadFileService;

    @Transactional(readOnly = true)
    public Work findWorkById(Long id) {
        return workRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no work that id:" + id));
    }

    @Transactional(readOnly = true)
    public List<Work> findWorkByMember(Member member) {
        return workRepository.findByMember(member);
    }

    @Transactional(readOnly = true)
    public List<WorkSimpleDto> findSimpleWorkByMember(Member member) {
        return workRepository.findSimpleByMember(member);
    }
    @Transactional
    public Work createWork(Work work) {
        return workRepository.save(work);
    }

    /**
     * uploadFile 자동생성을 위해 @Transactional 안에서 생성
     */
    @Transactional
    public Work createWork(Member loginUser,
                           WorkType workType,
                           String originUrl,
                           String title,
                           String description,
                           Size size,
                           String material,
                           Integer prodYear,
                           String price,
                           String collection
    ) {
        Work work = new WorkBuilder()
                .member(loginUser)
                .workType(workType)
                .originUrl(originUrl)
                .title(title)
                .description(description)
                .size(size)
                .material(material)
                .prodYear(prodYear)
                .price(price)
                .collection(collection)
                .build();
        return workRepository.save(work);
    }

    @Transactional
    public Work updateWork(Long workId,
                           WorkType workType,
                           String originUrl,
                           String title,
                           String description,
                           Size size,
                           String material,
                           Integer prodYear,
                           String price,
                           String collection
    ) {
        Work work = findWorkById(workId);
        if (!work.getWorkType().equals(workType)) work.setWorkType(workType);
        if (!work.getUploadFile().getOriginUrl().equals(originUrl)) {
            uploadFileService.deleteUploadFile(work.getUploadFile());
            work.setUploadFileUrl(originUrl);
        }
        if (!work.getTitle().equals(title)) work.setTitle(title);
        if (!work.getDescription().equals(description)) work.setDescription(description);
        if (!work.getSize().equals(size)) work.setSize(size);
        if (!work.getMaterial().equals(material)) work.setMaterial(material);
        if (!work.getProdYear().equals(prodYear)) work.setProdYear(prodYear);
        if (!work.getPrice().equals(price)) work.setPrice(price);
        if (!work.getCollection().equals(collection)) work.setCollection(collection);

        return work;
    }

    @Transactional
    public void removeWork(Work work) {
        uploadFileService.deleteUploadFile(work.getUploadFile());
        workRepository.delete(work);
    }
    @Transactional
    public void removeWorkById(Long workId) {
        Work target = findWorkById(workId);
        workRepository.delete(target);
    }
}

package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.builder.WorkBuilder;
import com.architrave.portfolio.domain.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class WorkService {

    private final WorkRepository workRepository;

    @Transactional(readOnly = true)
    public Work findWorkById(Long id) {
        return workRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no work that id:" + id));
    }

    @Transactional(readOnly = true)
    public List<Work> findWorkByMember(Member member) {
        return workRepository.findByMember(member);
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
                           String originUrl,
                           String thumbnailUrl,
                           String title,
                           String description,
                           Size size,
                           String material,
                           Integer prodYear
    ) {
        Work work = new WorkBuilder()
                .member(loginUser)
                .originUrl(originUrl)
                .thumbnailUrl(thumbnailUrl)
                .title(title)
                .description(description)
                .size(size)
                .material(material)
                .prodYear(prodYear)
                .build();
        return workRepository.save(work);
    }

    @Transactional
    public Work updateWork(Long workId,
                           String originUrl,
                           String thumbnailUrl,
                           String title,
                           String description,
                           Size size,
                           String material,
                           Integer prodYear
    ) {
        Work work = findWorkById(workId);
        if(originUrl != null || thumbnailUrl != null){
            work.setUploadFileUrl(originUrl, thumbnailUrl);
        }
        if(title != null) work.setTitle(title);
        if(description != null) work.setDescription(description);
        if(size != null) work.setSize(size);
        if(material != null) work.setMaterial(material);
        if(prodYear != null) work.setProdYear(prodYear);

        return work;
    }


}

package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.BillboardBuilder;
import com.architrave.portfolio.domain.repository.BillboardRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Trace
@Service
@RequiredArgsConstructor
public class BillboardService {

    private final BillboardRepository billboardRepository;

    @Transactional
    public Billboard createLb(Billboard billboard) {
        Billboard createdLb = billboardRepository.save(billboard);
        return createdLb;
    }

    @Transactional
    public Billboard updateLb(
            Long billboardId,
            String originUrl,
            String thumbnailUrl,
            String title,
            String description,
            Boolean isVisible
    ) {
        Billboard billboard = findLbById(billboardId);
        if(isVisible != null) billboard.setIsVisible(isVisible);
        if(originUrl != null || thumbnailUrl != null){
            billboard.setUploadFileUrl(originUrl, thumbnailUrl);
        }
        if(title != null)           billboard.setTitle(title);
        if(description != null)     billboard.setDescription(description);
        return billboard;
    }

    /**
     * 내부용
     * @param  billboardId
     * @return Billboard
     */
    @Transactional(readOnly = true)
    public Billboard findLbById(Long billboardId) {
        return billboardRepository.findById(billboardId)
                .orElseThrow(() -> new NoSuchElementException("there is no billboard that id: " + billboardId));
    }

    /**
     * @param  member
     * @return Billboard
     */
    @Transactional
    public Billboard findByMember(Member member) {
        Billboard billboard = billboardRepository.findByMember(member)
                .orElse(null);
        if(billboard != null){
            return billboard;
        }
        //default 생성
        Billboard defaultLb = new BillboardBuilder()
                .member(member)
                .originUrl("")
                .thumbnailUrl("")
                .title("Billboard")
                .description("Brief description of your content")
                .build();

        return billboardRepository.save(defaultLb);
    }
}

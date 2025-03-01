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

    private final UploadFileService uploadFileService;


    @Transactional
    public Billboard createLb(Billboard billboard) {
        Billboard createdLb = billboardRepository.save(billboard);
        return createdLb;
    }

    @Transactional
    public Billboard updateLb(
            Long billboardId,
            String originUrl,
            String title,
            String description,
            Boolean isVisible
    ) {
        Billboard billboard = findLbById(billboardId);
        if(!billboard.getIsVisible().equals(isVisible)) billboard.setIsVisible(isVisible);
        if(
                !billboard.getUploadFile().getOriginUrl().equals(originUrl)
        ){
            // S3에 있는 기존 S3 이미지 제거
            // 제거하지 않으면 DB 상에서 해당 이미지 url은 사라지고
            // orphan 객체가 되어버린다.
            uploadFileService.deleteUploadFile(billboard.getUploadFile());
            billboard.setUploadFileUrl(originUrl);
        }
        if(!billboard.getTitle().equals(title)) billboard.setTitle(title);
        if(!billboard.getDescription().equals(description)) billboard.setDescription(description);
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
                .title("Billboard")
                .description("Brief description of your content")
                .build();

        return billboardRepository.save(defaultLb);
    }

    @Transactional
    public void removeByMember(Member member) {
        billboardRepository.deleteByMember(member);
    }
}

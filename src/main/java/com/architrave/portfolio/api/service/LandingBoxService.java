package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.LandingBox;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.LandingBoxBuilder;
import com.architrave.portfolio.domain.repository.LandingBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LandingBoxService {

    private final LandingBoxRepository landingBoxRepository;

    @Transactional
    public LandingBox createLb(LandingBox landingBox) {
        LandingBox createdLb = landingBoxRepository.save(landingBox);
        return createdLb;
    }

    @Transactional
    public LandingBox updateLb(
            Long landingBoxId,
            String originUrl,
            String thumbnailUrl,
            String title,
            String description,
            Boolean isDeleted
    ) {
        LandingBox landingBox = findLbById(landingBoxId);
        if(isDeleted != null && isDeleted == true){
            landingBox.removeUploadFile();
            return landingBox;
        }
        if(originUrl != null || thumbnailUrl != null){
            landingBox.setUploadFileUrl(originUrl, thumbnailUrl);
        }
        if(title != null)           landingBox.setTitle(title);
        if(description != null)     landingBox.setDescription(description);
        if(landingBox.getIsDeleted()) landingBox.setIsDeleted(false);
        landingBox.setIsDeleted(false);
        return landingBox;
    }

    /**
     * 내부용
     * @param  landingBoxId
     * @return LandingBox
     */
    @Transactional(readOnly = true)
    public LandingBox findLbById(Long landingBoxId) {
        return landingBoxRepository.findById(landingBoxId)
                .orElseThrow(() -> new NoSuchElementException("there is no landingBox that id: " + landingBoxId));
    }

    /**
     * @param  member
     * @return LandingBox
     */
    @Transactional
    public LandingBox findByMember(Member member) {
        LandingBox landingBox = landingBoxRepository.findByMember(member)
                .orElse(null);
        if(landingBox != null){
            return landingBox;
        }
        //default 생성
        LandingBox defaultLb = new LandingBoxBuilder()
                .member(member)
                .originImgUrl("defaultUrl")
                .thumbnailUrl("defaultThumb")
                .title("default title")
                .description("default description")
                .build();

        landingBoxRepository.save(defaultLb);

        return defaultLb;
    }
}

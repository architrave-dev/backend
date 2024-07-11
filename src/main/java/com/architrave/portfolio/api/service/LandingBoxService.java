package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.LandingBox;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.repository.LandingBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LandingBoxService {

    private final LandingBoxRepository landingBoxRepository;
    private final AuthService authService;

    @Transactional
    public LandingBox createLb(LandingBox landingBox) {

        Member member = authService.getMemberFromContext();

        LandingBox createdLb = landingBoxRepository.save(landingBox);
        //Member와 연결
        member.setLandingBox(createdLb);
        return createdLb;
    }

    @Transactional
    public LandingBox updateLb(
            Member member,
            String originUrl,
            String thumbnailUrl,
            String title,
            String description,
            Boolean isDeleted
    ) {
        LandingBox landingBox = member.getLandingBox();
        if(isDeleted){
            landingBox.removeUploadFile();
            return landingBox;
        }
        if(originUrl != null || thumbnailUrl != null){
            landingBox.setUploadFileUrl(originUrl, thumbnailUrl);
        }
        if(title != null)           landingBox.setTitle(title);
        if(description != null)     landingBox.setDescription(description);
        if(landingBox.getIsDeleted()) landingBox.setIsDeleted(false);

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
                .orElse(null);
    }


}

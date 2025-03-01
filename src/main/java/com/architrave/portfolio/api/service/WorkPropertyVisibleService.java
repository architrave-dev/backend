package com.architrave.portfolio.api.service;


import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.WorkPropertyVisible;
import com.architrave.portfolio.domain.repository.WorkPropertyVisibleRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Trace
@Service
@RequiredArgsConstructor
public class WorkPropertyVisibleService {

    private final WorkPropertyVisibleRepository workPropertyVisibleRepository;

    @Transactional(readOnly = true)
    public WorkPropertyVisible findWPVById(Long id) {
        return workPropertyVisibleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no WorkPropertyVisible that id:" + id));
    }
    @Transactional
    public WorkPropertyVisible findWPVByMember(Member member) {
        WorkPropertyVisible found = workPropertyVisibleRepository.findByMember(member)
                .orElse(null);
        if(found != null){
            return found;
        }
        //default 생성
        WorkPropertyVisible defaultWpv = workPropertyVisibleRepository.save(new WorkPropertyVisible(member));

        return defaultWpv;
    }

    @Transactional
    public WorkPropertyVisible updateWorkPropertyVisible(
            Long workPropertyVisibleId,
            Boolean workType,
            Boolean imageUrl,
            Boolean description,
            Boolean price,
            Boolean collection
    ){
        WorkPropertyVisible wpv = findWPVById(workPropertyVisibleId);
        if(workType != null) wpv.setWorkType(workType);
        if(imageUrl != null) wpv.setImageUrl(imageUrl);
        if(description != null) wpv.setDescription(description);
        if(price != null) wpv.setPrice(price);
        if(collection != null) wpv.setCollection(collection);
        return wpv;
    }
    @Transactional
    public void removeByMember(Member member) {
        workPropertyVisibleRepository.deleteByMember(member);
    }
}

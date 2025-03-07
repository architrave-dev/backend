package com.architrave.portfolio.api.service;

import com.architrave.portfolio.api.dto.reorder.request.ReorderReq;
import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.ProjectElement;
import com.architrave.portfolio.domain.model.builder.CareerBuilder;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import com.architrave.portfolio.domain.repository.CareerRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Trace
@Service
@RequiredArgsConstructor
public class CareerService {
    private final CareerRepository careerRepository;

    @Transactional(readOnly = true)
    public Career findCareerById(Long careerId) {
        return careerRepository.findById(careerId)
                .orElseThrow(() -> new NoSuchElementException("there is no career that id" + careerId));
    }

    @Transactional(readOnly = true)
    public List<Career> findCareerByMember(Member member) {
        return careerRepository.findByMemberOrderByIndexAsc(member);
    }

    @Transactional
    public Career createCareer(Member loginUser, CareerType careerType, String content, Integer yearFrom) {
        Career career = new CareerBuilder()
                .member(loginUser)
                .careerType(careerType)
                .content(content)
                .yearFrom(yearFrom)
                .build();
        return careerRepository.save(career);
    }

    @Transactional
    public Career createCareer(Career career) {
        return careerRepository.save(career);
    }
    @Transactional
    public Career updateCareer(Long careerId,
                               String content,
                               Integer yearFrom) {
        Career career = findCareerById(careerId);
        if(content != null)  career.setContent(content);
        if(yearFrom != null)  career.setYearFrom(yearFrom);

        return career;
    }

    @Transactional
    public void removeCareerById(Long careerId) {
        Career career = findCareerById(careerId);
        careerRepository.delete(career);
    }

    @Transactional
    public void removeByMember(Member member) {
        careerRepository.deleteByMember(member);
    }

    @Transactional
    public List<Career> reorder(Member owner, CareerType careerType, List<ReorderReq> reorderReqList) {
        Map<Long, Integer> reorderMap = reorderReqList.stream()
                .collect(Collectors.toMap(ReorderReq::getId, ReorderReq::getIndex));

        List<Career> careerList = careerRepository.findByMemberAndCareerType(owner, careerType);

        for (Career career : careerList) {
            Integer newIndex = reorderMap.get(career.getId());
            if(newIndex != null){
                if (career.getIndex() == null || !newIndex.equals(career.getIndex())) {
                    career.setIndex(newIndex);
                }
            }
        }

        return careerRepository.saveAll(careerList);
    }
}

package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.CareerBuilder;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import com.architrave.portfolio.domain.repository.CareerRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

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
        return careerRepository.findByMember(member);
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
}

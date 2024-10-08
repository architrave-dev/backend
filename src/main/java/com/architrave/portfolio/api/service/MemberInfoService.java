package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.domain.model.builder.MemberInfoBuilder;
import com.architrave.portfolio.domain.model.enumType.CountryType;
import com.architrave.portfolio.domain.repository.MemberInfoRepository;
import com.architrave.portfolio.global.aop.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Trace
@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;

    @Transactional
    public MemberInfo createLb(MemberInfo memberInfo) {
        MemberInfo createdLb = memberInfoRepository.save(memberInfo);
        return createdLb;
    }

    @Transactional
    public MemberInfo updateMI(
            Long memberInfoId,
            String originUrl,
            String thumbnailUrl,
            String name,
            CountryType country,
            Integer year,
            String email,
            String contact,
            String description
    ) {
        MemberInfo memberInfo = findLbById(memberInfoId);
        if(originUrl != null || thumbnailUrl != null){
            memberInfo.setUploadFileUrl(originUrl, thumbnailUrl);
        }
        if(name != null)           memberInfo.setName(name);
        if(country != null)           memberInfo.setCountry(country);
        if(year != null)           memberInfo.setYear(year);
        if(email != null)           memberInfo.setEmail(email);
        if(contact != null)           memberInfo.setContact(contact);
        if(description != null)     memberInfo.setDescription(description);
        return memberInfo;
    }

    /**
     * 내부용
     * @param  memberInfoId
     * @return MemberInfo
     */
    @Transactional(readOnly = true)
    public MemberInfo findLbById(Long memberInfoId) {
        return memberInfoRepository.findById(memberInfoId)
                .orElseThrow(() -> new NoSuchElementException("there is no memberInfo that id: " + memberInfoId));
    }

    /**
     * @param  member
     * @return MemberInfo
     */
    @Transactional
    public MemberInfo findByMember(Member member) {
        MemberInfo memberInfo = memberInfoRepository.findByMember(member)
                .orElse(null);
        if(memberInfo != null){
            return memberInfo;
        }
        //default 생성
        MemberInfo defaultMI = new MemberInfoBuilder()
                .member(member)
                .originUrl("")
                .thumbnailUrl("")
                .name("")
                .email(member.getEmail())
                .year(1990)
                .contact("")
                .description("")
                .build();

        memberInfoRepository.save(defaultMI);

        return defaultMI;
    }
}

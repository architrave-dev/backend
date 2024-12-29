package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.domain.model.SocialMedia;
import com.architrave.portfolio.domain.model.builder.MemberInfoBuilder;
import com.architrave.portfolio.domain.model.enumType.CountryType;
import com.architrave.portfolio.domain.repository.MemberInfoRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Trace
@Service
@RequiredArgsConstructor
public class MemberInfoService {

    private final MemberInfoRepository memberInfoRepository;
    private final UploadFileService uploadFileService;

    @Transactional
    public MemberInfo createMI(MemberInfo memberInfo) {
        MemberInfo createdMI = memberInfoRepository.save(memberInfo);
        return createdMI;
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
        MemberInfo memberInfo = findMIById(memberInfoId);
        if (
                !memberInfo.getUploadFile().getOriginUrl().equals(originUrl) ||
                !memberInfo.getUploadFile().getThumbnailUrl().equals(thumbnailUrl)
        ) {
            // S3에 있는 기존 S3 이미지 제거
            // 제거하지 않으면 DB 상에서 해당 이미지 url은 사라지고
            // orphan 객체가 되어버린다.
            uploadFileService.deleteUploadFile(memberInfo.getUploadFile());
            memberInfo.setUploadFileUrl(originUrl, thumbnailUrl);
        }
        if(!memberInfo.getName().equals(name)) memberInfo.setName(name);
        if(!memberInfo.getCountry().equals(country)) memberInfo.setCountry(country);
        if(!memberInfo.getYear().equals(year)) memberInfo.setYear(year);
        if(!memberInfo.getEmail().equals(email)) memberInfo.setEmail(email);
        if(!memberInfo.getContact().equals(contact)) memberInfo.setContact(contact);
        if(!memberInfo.getDescription().equals(description)) memberInfo.setDescription(description);

        return memberInfo;
    }

    @Transactional
    public MemberInfo updateContact(
            Long memberInfoId,
            String address,
            String email,
            String contact,
            SocialMedia socialMedia
    ) {
        MemberInfo memberInfo = findMIById(memberInfoId);
        if(!memberInfo.getAddress().equals(address)) memberInfo.setAddress(address);
        if(!memberInfo.getEmail().equals(email)) memberInfo.setEmail(email);
        if(!memberInfo.getContact().equals(contact)) memberInfo.setContact(contact);
        if(!memberInfo.getSns().equals(socialMedia)) memberInfo.setSns(socialMedia);
        return memberInfo;
    }

    /**
     * 내부용
     * @param  memberInfoId
     * @return MemberInfo
     */
    @Transactional(readOnly = true)
    public MemberInfo findMIById(Long memberInfoId) {
        return memberInfoRepository.findById(memberInfoId)
                .orElseThrow(() -> new NoSuchElementException("there is no memberInfo that id: " + memberInfoId));
    }

    /**
     * @param  member
     * @return MemberInfo
     */
    @Transactional
    public MemberInfo findMIByMember(Member member) {
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
                .email("")
                .country(CountryType.NONE)
                .year(0)
                .contact("")
                .description("")
                .address("")
                .sns(new SocialMedia())
                .build();

        memberInfoRepository.save(defaultMI);

        return defaultMI;
    }
}

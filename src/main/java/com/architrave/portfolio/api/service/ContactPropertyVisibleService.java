package com.architrave.portfolio.api.service;


import com.architrave.portfolio.domain.model.ContactPropertyVisible;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.repository.ContactPropertyVisibleRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Trace
@Service
@RequiredArgsConstructor
public class ContactPropertyVisibleService {

    private final ContactPropertyVisibleRepository contactPropertyVisibleRepository;

    @Transactional(readOnly = true)
    public ContactPropertyVisible findCPVById(Long id) {
        return contactPropertyVisibleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("there is no ContactPropertyVisible that id:" + id));
    }
    @Transactional
    public ContactPropertyVisible findCPVByMember(Member member) {
        ContactPropertyVisible found = contactPropertyVisibleRepository.findByMember(member)
                .orElse(null);
        if(found != null){
            return found;
        }
        //default 생성
        ContactPropertyVisible defaultWpv = contactPropertyVisibleRepository.save(new ContactPropertyVisible(member));

        return defaultWpv;
    }

    @Transactional
    public ContactPropertyVisible updateCPV(
            Long contactPropertyVisibleId,
            Boolean address,
            Boolean email,
            Boolean contact,
            Boolean twitter,
            Boolean instagram,
            Boolean facebook,
            Boolean youtube,
            Boolean url1
    ){
        ContactPropertyVisible cpv = findCPVById(contactPropertyVisibleId);
        if(address != null) cpv.setAddress(address);
        if(email != null) cpv.setEmail(email);
        if(contact != null) cpv.setContact(contact);
        if(twitter != null) cpv.setTwitter(twitter);
        if(instagram != null) cpv.setInstagram(instagram);
        if(facebook != null) cpv.setFacebook(facebook);
        if(youtube != null) cpv.setYoutube(youtube);
        if(url1 != null) cpv.setUrl1(url1);
        return cpv;
    }

    @Transactional
    public void removeByMember(Member member) {
        contactPropertyVisibleRepository.deleteByMember(member);
    }
}

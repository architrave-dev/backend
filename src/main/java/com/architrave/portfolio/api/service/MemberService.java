package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.repository.MemberRepository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member createMember(Member member){
        if(memberRepository.existsByEmail(member.getEmail())){
            throw new IllegalArgumentException("already exist email");
        }
        return memberRepository.save(member);
    }

    @Transactional
    public void removeMember(Member member) {
        if(!memberRepository.existsByEmail(member.getEmail())){
            throw new NoSuchElementException("there is no member");
        }
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElse(null);
    }
    @Transactional(readOnly = true)
    public Member findMemberById(Member member) {
        return memberRepository.findById(member.getId())
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Member findMemberByAui(String aui) {
        return memberRepository.findByAui(aui)
                .orElse(null);
    }
}
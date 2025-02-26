package com.architrave.portfolio.api.service;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.enumType.MemberStatus;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.MemberRepository;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Trace
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
                .orElseThrow(() -> new NoSuchElementException("there is no Member that id: " + memberId));
    }

    @Transactional(readOnly = true)
    public Member findMemberByAui(String aui) {
        return memberRepository.findByAui(aui)
                .orElseThrow(() -> new NoSuchElementException("there is no Member that aui: " + aui));
    }

    /**
     * 현재는 Member 변경로직 없다.
     * 향후 password, username 등 추가될 예정
     */
    @Transactional
    public Member updateMemberRole(Long memberId,
                             RoleType role
    ) {
        Member member = findMemberById(memberId);
        member.setRole(role);
        return member;
    }

    @Transactional
    public void changeStatus(String email, MemberStatus status) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        member.setStatus(status);
    }
}

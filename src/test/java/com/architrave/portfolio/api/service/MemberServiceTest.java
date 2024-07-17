package com.architrave.portfolio.api.service;


import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@SpringBootTest
public class MemberServiceTest {

    private final MemberService memberService;
    private final EntityManager em;

    private final String TEST_MEMBER_EMAIL = "lee@gmail.com";
    private final String TEST_MEMBER_EMAIL_2 = "jung@gmail.com";
    private final String TEST_MEMBER_PASSWORD = "12345";
    private final String TEST_MEMBER_USERNAME = "이중섭";
    private final String TEST_MEMBER_USERNAME_CHANGED = "중섭이";
    private final RoleType ROLE_USER = RoleType.USER;
    private final RoleType ROLE_CHANGED = RoleType.ADMIN;

    @Autowired
    public MemberServiceTest(MemberService memberService, EntityManager em) {
        this.memberService = memberService;
        this.em = em;
    }

    @Test
    @Transactional
    public void createTest(){
        //given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();

        //when
        Member afterCreateMember = memberService.createMember(member);
        em.flush();
        em.clear();

        //then
        Assertions.assertNotNull(afterCreateMember.getId());
        Assertions.assertEquals(afterCreateMember.getUsername(), TEST_MEMBER_USERNAME);
        Assertions.assertNotNull(afterCreateMember.getAui());
    }
    @Test
    @Transactional
    public void IllegalArgumentExceptionWhenCreateDuplicatedEmail(){
        //given
        Member member1 = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();

        Member member2 = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();

        //when then
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            memberService.createMember(member1);
            em.flush();
            em.clear();
            memberService.createMember(member2);
        });
    }
    @Test
    @Transactional
    public void IllegalArgumentExceptionWhenCreateWithEmptyUsernameAndAUI(){
        //when then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            //given
            Member member = new MemberBuilder()
                    .email(TEST_MEMBER_EMAIL)
                    .password(TEST_MEMBER_PASSWORD)
                    .role(ROLE_USER)
                    .build();
        });
    }
    @Test
    @Transactional
    public void removeTest(){
        //given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();

        Member afterCreateMember = memberService.createMember(member);
        em.flush();
        em.clear();

        //when
        memberService.removeMember(afterCreateMember);
        em.flush();
        em.clear();

        //then
        Assertions.assertNull(memberService.findMemberById(afterCreateMember));
    }
    @Test
    @Transactional
    public void NoSuchElementExceptionWhenRemoveEmpty(){
        //given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();

        //when then
        Assertions.assertThrows(NoSuchElementException.class,
                ()-> memberService.removeMember(member));
    }
    @Test
    @Transactional
    public void findWithIdTest(){
        //given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();

        Member afterCreateMember = memberService.createMember(member);
        em.flush();
        em.clear();

        //when
        Member findMember = memberService.findMemberById(afterCreateMember.getId());

        //then
        Assertions.assertEquals(afterCreateMember.getEmail(), findMember.getEmail());
        Assertions.assertEquals(afterCreateMember.getPassword(), findMember.getPassword());
        Assertions.assertEquals(afterCreateMember.getUsername(), findMember.getUsername());
        Assertions.assertEquals(afterCreateMember.getAui(), findMember.getAui());
        Assertions.assertEquals(afterCreateMember.getRole(), findMember.getRole());
    }
    @Test
    @Transactional
    public void findWithAUITest(){
        //given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();

        Member afterCreateMember = memberService.createMember(member);

        em.flush();
        em.clear();
        //when
        Member findMember = memberService.findMemberByAui(afterCreateMember.getAui());

        //then
        Assertions.assertEquals(afterCreateMember.getEmail(), findMember.getEmail());
        Assertions.assertEquals(afterCreateMember.getPassword(), findMember.getPassword());
        Assertions.assertEquals(afterCreateMember.getAui(), findMember.getAui());
        Assertions.assertEquals(afterCreateMember.getRole(), findMember.getRole());
        Assertions.assertEquals(afterCreateMember.getUsername(), findMember.getUsername());

    }
    @Test
    public void findDuplicateTest(){
        //같은 이메일의 member create가 막혀있기에 성립불가
    }
    @Test
    @Transactional
    public void findEmptyTest(){
        //given
        //when then
        Assertions.assertNull(memberService.findMemberById(1L));
    }





    @Test
    @Transactional
    public void updateRoleTest(){   //일반 update 테스트, 중복가능
        //given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();
        Member createdMember = memberService.createMember(member);

        //when
        createdMember.setRole(ROLE_CHANGED);
        em.flush();
        em.clear();

        Member afterUpdateMember = memberService.findMemberById(createdMember.getId());

        //then
        Assertions.assertEquals(afterUpdateMember.getId(), createdMember.getId());
        Assertions.assertEquals(afterUpdateMember.getAui(), createdMember.getAui());
        Assertions.assertEquals(afterUpdateMember.getRole(), ROLE_CHANGED);
    }
    @Test
    @Transactional
    public void updateUsernameTest(){   //중복가능, aui는 중복 불가
        //given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();
        Member createdMember = memberService.createMember(member);

        //when
        createdMember.setUsername(TEST_MEMBER_USERNAME_CHANGED);
        em.flush();
        em.clear();

        //then
        Assertions.assertEquals(createdMember.getId(), createdMember.getId());
        Assertions.assertEquals(createdMember.getUsername(), TEST_MEMBER_USERNAME_CHANGED);
        System.out.println(createdMember.getAui());
    }
    @Test
    @Transactional
    public void updateDuplicateUsernameTest(){   //중복가능, aui는 중복 불가
        //given
        Member member1 = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();

        Member member2 = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL_2)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME_CHANGED)
                .role(ROLE_USER)
                .build();
        memberService.createMember(member1);
        Member createdMember2 = memberService.createMember(member2);
        em.flush();
        em.clear();

        //when
        Member findMember = memberService.findMemberById(createdMember2.getId());
        findMember.setUsername(TEST_MEMBER_USERNAME);
        em.flush();
        em.clear();

        //then
        Assertions.assertNotEquals(findMember.getUsername(), createdMember2.getUsername());
        Assertions.assertEquals(findMember.getUsername(), TEST_MEMBER_USERNAME);
        Assertions.assertEquals(findMember.getId(), createdMember2.getId());
        Assertions.assertNotNull(findMember.getAui());

    }

}

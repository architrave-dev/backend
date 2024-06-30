package com.architrave.portfolio;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberTest {

    private MemberService memberService;

    @Autowired
    public MemberTest(MemberService memberService) {
        this.memberService = memberService;
    }

    @Test
    public void createTest(){
        //given
        //when
        //then
    }
    @Test
    public void createDuplicateTest(){
        //given
        //when
        //then
    }
    @Test
    public void removeTest(){
        //given
        //when
        //then
    }
    @Test
    public void removeEmptyTest(){
        //given
        //when
        //then
    }
    @Test
    public void findWithIdTest(){
        //given
        //when
        //then
    }
    @Test
    public void findWithAUITest(){
        //given
        //when
        //then
    }
    @Test
    public void findDuplicateTest(){
        //given
        //when
        //then
    }
    @Test
    public void findEmptyTest(){
        //given
        //when
        //then
    }


}

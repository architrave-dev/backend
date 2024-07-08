package com.architrave.portfolio.api.security;

import com.architrave.portfolio.api.dto.auth.request.CreateMemberReq;
import com.architrave.portfolio.api.service.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAuthTest {

    private MemberService memberService;
    private EntityManager em;
    private MockMvc mockMvc;

    @Autowired
    public SecurityAuthTest(MemberService memberService, EntityManager em, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.em = em;
        this.mockMvc = mockMvc;
    }

    @Test
    public void signinTest(){

    }

    @Test
    public void signinAlreadyEmailTest(){
        //given
        //when
        //then
    }

    @Test
    public void loginTest(){
        //given
        //when
        //then
    }

    @Test
    public void loginNoEmailTest(){
        //given
        //when
        //then
    }

    @Test
    public void loginWrongPwTest(){
        //given
        //when
        //then
    }
}

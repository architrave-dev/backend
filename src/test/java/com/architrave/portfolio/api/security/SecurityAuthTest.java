package com.architrave.portfolio.api.security;

import com.architrave.portfolio.api.dto.auth.request.CreateMemberReq;
import com.architrave.portfolio.api.dto.auth.request.LoginReq;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityAuthTest {

    private MemberService memberService;
    private EntityManager em;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public SecurityAuthTest(MemberService memberService, EntityManager em, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.memberService = memberService;
        this.em = em;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    private final String TEST_MEMBER_EMAIL = "lee@gmail.com";
    private final String TEST_MEMBER_PASSWORD = "12345";
    private final String TEST_MEMBER_PASSWORD_WRONG = "6789";
    private final String TEST_MEMBER_USERNAME = "이중섭";
    private final RoleType ROLE_USER = RoleType.USER;


    private void injectDefaultMember(){
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(ROLE_USER)
                .build();

        Member afterCreateMember = memberService.createMember(member);
        em.flush();
        em.clear();
        System.out.println("default member created");
    }

    @Test
    @Transactional
    public void signinTest() throws Exception {
        //given
        CreateMemberReq request = new CreateMemberReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD,
                TEST_MEMBER_USERNAME
        );
        String createJson = objectMapper.writeValueAsString(request);

        //when then
        mockMvc.perform(
                post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson)
        ).andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void BadRequestWhenAlreadyEmailExist() throws Exception {
        //given
        injectDefaultMember();
        CreateMemberReq request = new CreateMemberReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD,
                TEST_MEMBER_USERNAME
        );
        String createJson = objectMapper.writeValueAsString(request);

        //when then
        mockMvc.perform(
                post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        Assertions.assertTrue(
                                result.getResolvedException()
                                        instanceof IllegalArgumentException)
                );
    }

    @Test
    @Transactional
    public void loginTest() throws Exception {
        //given
        injectDefaultMember();
        LoginReq request = new LoginReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD
        );
        String createJson = objectMapper.writeValueAsString(request);

        //when then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson))
                .andExpect(status().isOk())
                .andExpect(header().exists("Authorization"))
                .andExpect(header().string("Authorization", startsWith("Bearer ")));
    }

    @Test
    @Transactional
    public void BadRequestWhenMemberEmpty() throws Exception{
        //given
        LoginReq request = new LoginReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD
        );
        String createJson = objectMapper.writeValueAsString(request);

        //when then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        Assertions.assertTrue(
                                result.getResolvedException()
                                        instanceof UsernameNotFoundException)
                );
    }

    @Test
    @Transactional
    public void UnauthorizedWhenWrongPw() throws Exception{
        //given
        injectDefaultMember();
        LoginReq request = new LoginReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD_WRONG
        );
        String createJson = objectMapper.writeValueAsString(request);

        //when then
        mockMvc.perform(
                        post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(createJson))
                .andExpect(status().isUnauthorized())
                .andExpect(result ->
                        Assertions.assertTrue(
                                result.getResolvedException()
                                        instanceof BadCredentialsException)
                );
    }
}

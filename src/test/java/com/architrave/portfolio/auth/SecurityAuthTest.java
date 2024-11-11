package com.architrave.portfolio.auth;

import com.architrave.portfolio.api.dto.auth.request.CreateMemberReq;
import com.architrave.portfolio.api.dto.auth.request.LoginReq;
import com.architrave.portfolio.api.dto.auth.request.RefreshReq;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.domain.repository.MemberRepository;
import com.architrave.portfolio.infra.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityAuthTest {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void cleanUp() {
        memberRepository.deleteAll();
    }
    private final String TEST_MEMBER_EMAIL = "lee@gmail.com";
    private final String TEST_MEMBER_PASSWORD = "12345";
    private final String TEST_MEMBER_PASSWORD_WRONG = "6789";
    private final String TEST_MEMBER_USERNAME = "이중섭";
    private final RoleType ROLE_USER = RoleType.USER;


    @Test
    @DisplayName("signin: should return string 'signin success'")
    public void signinTest() throws Exception {
        // when
        CreateMemberReq createMemberReq = new CreateMemberReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD,
                TEST_MEMBER_USERNAME);

        // then
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("signin success"));
    }

    @Test
    @DisplayName("signin: existed email should return BadRequest Exception")
    public void BadRequestWhenAlreadyEmailExist() throws Exception {
        //Given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(TEST_MEMBER_PASSWORD)
                .username(TEST_MEMBER_USERNAME)
                .role(RoleType.USER)
                .build();
        memberService.createMember(member);

        //When
        CreateMemberReq createMemberReq = new CreateMemberReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD,
                TEST_MEMBER_USERNAME);

        //Then
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createMemberReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("login:  should return AccessToken in Authorization header")
    public void loginTest() throws Exception {
        // Prepare the login request
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(passwordEncoder.encode(TEST_MEMBER_PASSWORD))
                .username(TEST_MEMBER_USERNAME)
                .role(RoleType.USER)
                .build();
        memberService.createMember(member);

        LoginReq loginReq = new LoginReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD
        );

        // Perform the login request
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", startsWith("Bearer ")));
    }

    @Test
    @DisplayName("login: empty member should return BadRequest Exception")
    public void BadRequestWhenMemberEmpty() throws Exception{
        //When
        LoginReq loginReq = new LoginReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD
        );

        // Then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("login: wrong PW should return Unauthorized Exception")
    public void UnauthorizedWhenWrongPw() throws Exception{
        //Given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(passwordEncoder.encode(TEST_MEMBER_PASSWORD))
                .username(TEST_MEMBER_USERNAME)
                .role(RoleType.USER)
                .build();
        memberService.createMember(member);

        //When
        LoginReq loginReq = new LoginReq(
                TEST_MEMBER_EMAIL,
                TEST_MEMBER_PASSWORD_WRONG
        );

        // Then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    @DisplayName("if AccessToken expired, should return ATX Exception")
    public void ATXWhenExpiredAccessToken() throws Exception{
    }

    @Test
    @DisplayName("refresh:  should return new accessToken")
    public void refreshAccessToken() throws Exception{
        //Given
        Member member = new MemberBuilder()
                .email(TEST_MEMBER_EMAIL)
                .password(passwordEncoder.encode(TEST_MEMBER_PASSWORD))
                .username(TEST_MEMBER_USERNAME)
                .role(RoleType.USER)
                .build();
        memberService.createMember(member);

        // When
        String refreshToken = jwtService.createRefreshToken(member);
        RefreshReq refreshReq = new RefreshReq(
                refreshToken
        );

        // Perform the refresh token request
        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshReq)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", startsWith("Bearer ")));
    }

    @Test
    @DisplayName("refresh: if refreshToken expired should return RTX")
    public void RTXWhenExpiredRefreshToken() throws Exception{

    }
}

package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.auth.request.CreateMemberReq;
import com.architrave.portfolio.api.dto.auth.request.LoginReq;
import com.architrave.portfolio.api.dto.auth.response.MemberSimpleDto;
import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@Tag(name = "1. Auth")  // => swagger 이름
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;


    @Operation(
            summary = "회원가입",
            description = "회원가입을 위한 API 입니다."
    )
    @PostMapping("/signin")
    public ResponseEntity<ResultDto<String>> signin(@RequestBody CreateMemberReq createMemberReq){

        Member member = new MemberBuilder()
                .email(createMemberReq.getEmail())
                .password(createMemberReq.getPassword())
                .username(createMemberReq.getUsername())
                .role(RoleType.USER)
                .build();

        memberService.createMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("signin success"));
    }

    @Operation(
            summary = "로그인",
            description = "로그인을 위한 API 입니다."
    )
    @PostMapping("/login")
    public ResponseEntity<ResultDto<MemberSimpleDto>> login(@RequestBody LoginReq loginReq){

        String email = loginReq.getEmail();
        String password = loginReq.getPassword();

        Member member = authService.loadUserByUsername(email);
        String authHeader = authService.login(member, password);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .body(new ResultDto<>(new MemberSimpleDto(member)));
    }

    @Operation(
            summary = "[미지원] 로그아웃",
            description = "로그아웃은 jwt token을 지우는 것으로 대체합니다."
    )
    @GetMapping("/logout")
    public void login(){}
}

package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.auth.request.ActivateReq;
import com.architrave.portfolio.api.dto.auth.request.CreateMemberReq;
import com.architrave.portfolio.api.dto.auth.request.LoginReq;
import com.architrave.portfolio.api.dto.auth.request.RefreshReq;
import com.architrave.portfolio.api.dto.auth.response.MemberSimpleDto;
import com.architrave.portfolio.api.dto.auth.response.SimpleStringDto;
import com.architrave.portfolio.api.dto.email.request.EmailReq;
import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.api.service.EmailService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.MemberStatus;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;



@Tag(name = "01. Auth")  // => swagger 이름
@Trace
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final AuthService authService;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final OwnerContextHolder ownerContextHolder;


    @Operation(
            summary = "회원가입",
            description = "회원가입을 위한 API 입니다. <br />" +
                    "중복 가능한 username으로 unique한 aui를 생성합니다." +
                    "회원가입 후 이메일 인증 링크를 발송합니다."
    )
    @PostMapping("/signin")
    public ResponseEntity<ResultDto<SimpleStringDto>> signin(@Valid @RequestBody CreateMemberReq createMemberReq){
        Member member = new MemberBuilder()
                .email(createMemberReq.getEmail())
                .password(passwordEncoder.encode(createMemberReq.getPassword()))
                .username(createMemberReq.getUsername())
                .role(RoleType.USER)
                .status(MemberStatus.PENDING)
                .build();

        memberService.createMember(member);
//        String verificationCode = String.format("%06d", (int)(Math.random() * 1000000));
        String verificationCode = "123456";
        authService.createVerification(member.getEmail(), verificationCode);
        emailService.sendVerificationEmail(member.getEmail(), verificationCode);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new SimpleStringDto("Registration completes upon email verification.", createMemberReq.getEmail())));
    }
    @Operation(
            summary = "이메일 인증 완료",
            description = "이메일 인증 토큰으로 Member을 활성화합니다."
    )
    @PostMapping("/activate")
    public ResponseEntity<ResultDto<String>> activateUser(@Valid @RequestBody ActivateReq activateReq){

        authService.verify(activateReq.getKey(), activateReq.getVerificationCode());

        memberService.changeStatus(activateReq.getKey(), MemberStatus.ACTIVE);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("Email verification is complete. Please log in."));
    }
    @Operation(
            summary = "로그인",
            description = "로그인을 위한 API 입니다. <br />" +
                    "Authorization 헤더에 jwt 토큰을 반환합니다."
    )
    @PostMapping("/login")
    public ResponseEntity<ResultDto<MemberSimpleDto>> login(@Valid @RequestBody LoginReq loginReq){
        String email = loginReq.getEmail();
        String password = loginReq.getPassword();
        Member member = authService.loadUserByUsername(email);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        password
                )
        );

        member.validateActiveStatus();

        String authHeader = authService.getAccessToken(member);
        String refreshToken = authService.getRefreshToken(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .body(new ResultDto<>(new MemberSimpleDto(member, refreshToken)));
    }

    @Operation(
            summary = "refresh accessToken",
            description = "기존의 jwt 토큰이 만료되었다면 refreshToken을 검증합니다. <br />" +
                    "Authorization 헤더에 새로운 jwt 토큰을 반환합니다."
    )
    @PostMapping("/refresh")
    public ResponseEntity<ResultDto<String>> refresh(@Valid @RequestBody RefreshReq refreshReq){
        String refreshToken = refreshReq.getRefreshToken();
        String authHeader = authService.refreshToken(refreshToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .body(new ResultDto<>("refresh success"));
    }

    @Operation(
            summary = "[미지원] 로그아웃",
            description = "로그아웃은 jwt token을 지우는 것으로 대체합니다."
    )
    @Deprecated
    @GetMapping("/logout")
    public void login(){}

    @Deprecated
    @PostMapping("/admin/register")
    public ResponseEntity<ResultDto<String>> adminRegister(@Valid @RequestBody CreateMemberReq createMemberReq){
        Member member = new MemberBuilder()
                .email(createMemberReq.getEmail())
                .password(passwordEncoder.encode(createMemberReq.getPassword()))
                .username(createMemberReq.getUsername())
                .role(RoleType.USER)
                .status(MemberStatus.ACTIVE)
                .build();

        memberService.createMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("admin register complete"));
    }
}

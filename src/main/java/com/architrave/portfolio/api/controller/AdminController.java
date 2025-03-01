package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.auth.request.CreateMemberReq;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.WorkPropertyVisible;
import com.architrave.portfolio.domain.model.builder.MemberBuilder;
import com.architrave.portfolio.domain.model.enumType.MemberStatus;
import com.architrave.portfolio.domain.model.enumType.RoleType;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@Trace
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final SettingService settingService;
    private final BillboardService billboardService;
    private final WorkService workService;
    private final CareerService careerService;
    private final MemberInfoService memberInfoService;
    private final ProjectService projectService;
    private final WorkPropertyVisibleService workPropertyVisibleService;


    @Deprecated
    @PostMapping("/register")
    public ResponseEntity<ResultDto<String>> adminRegister(@Valid @RequestBody CreateMemberReq createMemberReq){
        Member member = new MemberBuilder()
                .email(createMemberReq.getEmail())
                .password(passwordEncoder.encode(createMemberReq.getPassword()))
                .username(createMemberReq.getUsername())
                .role(RoleType.USER)
                .status(MemberStatus.ACTIVE)
                .build();

        Member createdMember = memberService.createMember(member);

        settingService.findSettingByMember(createdMember);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("admin register complete"));
    }
    @Deprecated
    @DeleteMapping("/delete")
    @Transactional
    public ResponseEntity<ResultDto<String>> adminCancel(
            @RequestParam("memberId") Long memberId
    ){
        Member member = memberService.findMemberById(memberId);

        //member 관련 모든 entity 삭제
        settingService.removeByMember(member);
        billboardService.removeByMember(member);
        workPropertyVisibleService.removeByMember(member);
        careerService.removeByMember(member);
        memberInfoService.removeByMember(member);


//        workService.removeByMember(member);
//        projectService.removeByMember(member);


        memberService.removeMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("admin delete complete"));
    }
}

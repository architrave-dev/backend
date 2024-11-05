package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.memberInfo.request.UpdateMemberInfoReq;
import com.architrave.portfolio.api.dto.memberInfo.response.MemberInfoDto;
import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.api.service.MemberInfoService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "10. MemberInfo")  // => swagger 이름
@Trace
@RestController
@RequestMapping("/api/v1/member-info")
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberInfoService memberInfoService;
    private final MemberService memberService;
    private final AuthService authService;

    @Operation(summary = "작가의 MemberInfo 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<MemberInfoDto>> getMemberInfo(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);
        MemberInfo memberInfo = memberInfoService.findByMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new MemberInfoDto(memberInfo)));
    }

    @Operation(summary = "작가의 MemberInfo 수정하기")
    @PutMapping
    public ResponseEntity<ResultDto<MemberInfoDto>> updateMemberInfo(
            @RequestParam("aui") String aui,    // 현재 홈페이지 주인
            @Valid @RequestBody UpdateMemberInfoReq updateMemberInfoReq
    ){
        Member loginUser = authService.getMemberFromContext();
        //aui와 현재 로그인 한 Member가 같은 사람인지 확인
        //=> Security에서 처리할 수 있나?
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        MemberInfo updatedMI = memberInfoService.updateMI(
                updateMemberInfoReq.getId(),
                updateMemberInfoReq.getOriginUrl(),
                updateMemberInfoReq.getThumbnailUrl(),
                updateMemberInfoReq.getName(),
                updateMemberInfoReq.getCountry(),
                updateMemberInfoReq.getYear(),
                updateMemberInfoReq.getEmail(),
                updateMemberInfoReq.getContact(),
                updateMemberInfoReq.getDescription()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new MemberInfoDto(updatedMI)));
    }
}

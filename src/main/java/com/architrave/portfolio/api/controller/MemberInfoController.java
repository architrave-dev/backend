package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.memberInfo.request.UpdateMemberInfoReq;
import com.architrave.portfolio.api.dto.memberInfo.response.MemberInfoDto;
import com.architrave.portfolio.api.service.MemberInfoService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "11. MemberInfo")  // => swagger 이름
@Trace
@RestController
@RequestMapping("/api/v1/member-info")
@RequiredArgsConstructor
public class MemberInfoController {

    private final MemberInfoService memberInfoService;
    private final MemberService memberService;

    @Operation(summary = "작가의 MemberInfo 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<MemberInfoDto>> getMemberInfo(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);
        MemberInfo memberInfo = memberInfoService.findMIByMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new MemberInfoDto(memberInfo)));
    }

    @Operation(summary = "작가의 MemberInfo 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<MemberInfoDto>> updateMemberInfo(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateMemberInfoReq updateMemberInfoReq
    ){
        MemberInfo updatedMI = memberInfoService.updateMI(
                updateMemberInfoReq.getId(),
                updateMemberInfoReq.getUpdateUploadFileReq().getOriginUrl(),
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

package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.auth.response.MemberSearchDto;
import com.architrave.portfolio.api.dto.auth.response.MemberSearchListDto;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.api.service.SettingService;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "02. Member")  // => swagger 이름
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;
    private final SettingService settingService;


    @Operation(summary = "작가 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<String>> getMember(
            @RequestParam("aui") String aui
    ){
        memberService.findMemberByAui(aui);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("ok"));
    }

    @Operation(summary = "username 기반 Member 검색")
    @GetMapping("/search")
    public ResponseEntity<ResultDto<MemberSearchListDto>> search(
            @RequestParam("query") String username
    ){
        List<MemberSearchDto> memberList = settingService.searchMembersByUsernamePrefix(username);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new MemberSearchListDto(memberList)));
    }
}

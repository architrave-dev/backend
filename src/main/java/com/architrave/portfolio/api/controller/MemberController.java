package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "2. Member")  // => swagger 이름
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;


    @Operation(summary = "작가 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<String>> getMember(
            @RequestParam("aui") String aui
    ){
        log.info("hello from getMember");

        memberService.findMemberByAui(aui);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("ok"));
    }
}

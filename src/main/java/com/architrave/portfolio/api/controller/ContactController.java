package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.contact.request.UpdateContactReq;
import com.architrave.portfolio.api.dto.contact.response.ContactDto;
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

@Tag(name = "11. Contact")  // => swagger 이름
@Trace
@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactController {

    private final MemberInfoService memberInfoService;
    private final MemberService memberService;

    @Operation(summary = "작가의 Contact 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<ContactDto>> getContact(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);
        MemberInfo memberInfo = memberInfoService.findMIByMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ContactDto(memberInfo)));
    }

    @Operation(summary = "작가의 Contact 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<ContactDto>> updateContact(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateContactReq updateContactReq
    ){
        MemberInfo updatedContact = memberInfoService.updateContact(
                updateContactReq.getId(),
                updateContactReq.getAddress(),
                updateContactReq.getEmail(),
                updateContactReq.getContact(),
                updateContactReq.getSns()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ContactDto(updatedContact)));
    }
}

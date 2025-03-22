package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.contact.request.UpdateContactPropertyVisibleReq;
import com.architrave.portfolio.api.dto.contact.response.ContactPropertyVisibleDto;
import com.architrave.portfolio.api.service.ContactPropertyVisibleService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.ContactPropertyVisible;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "13. ContactPropertyVisible")  // => swagger 이름
@Trace
@RestController
@RequestMapping("/api/v1/contact-property")
@RequiredArgsConstructor
public class ContactPropertyVisibleController {

    private final MemberService memberService;
    private final ContactPropertyVisibleService contactPropertyVisibleService;

    @Operation(summary = "ContactPropertyVisible 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<ContactPropertyVisibleDto>> getContactPropertyVisible(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);
        ContactPropertyVisible contactPropertyVisible = contactPropertyVisibleService.findCPVByMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ContactPropertyVisibleDto(contactPropertyVisible)));
    }

    @Operation(summary = "ContactPropertyVisible 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<ContactPropertyVisibleDto>> updateContactPropertyVisible(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateContactPropertyVisibleReq updateContactPropertyVisibleReq
    ){
        ContactPropertyVisible updatedCPV = contactPropertyVisibleService.updateCPV(
                updateContactPropertyVisibleReq.getContactPropertyVisibleId(),
                updateContactPropertyVisibleReq.getAddress(),
                updateContactPropertyVisibleReq.getEmail(),
                updateContactPropertyVisibleReq.getContact(),
                updateContactPropertyVisibleReq.getTwitter(),
                updateContactPropertyVisibleReq.getInstagram(),
                updateContactPropertyVisibleReq.getFacebook(),
                updateContactPropertyVisibleReq.getYoutube(),
                updateContactPropertyVisibleReq.getUrl1()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new ContactPropertyVisibleDto(updatedCPV)));
    }
}

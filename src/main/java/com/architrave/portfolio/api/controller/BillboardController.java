package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.billboard.request.UpdateBillboardDto;
import com.architrave.portfolio.api.dto.billboard.response.BillboardDto;
import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.api.service.BillboardService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.global.aop.Trace;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "8. Billboard")  // => swagger 이름
@Trace
@RestController
@RequestMapping("/api/v1/billboard")
@RequiredArgsConstructor
public class BillboardController {

    private final BillboardService billboardService;
    private final MemberService memberService;
    private final AuthService authService;

    @Operation(
            summary = "작가의 Billboard 조회하기",
            description = "Billboard가 없을 경우 자동생성됩니다."
    )
    @GetMapping
    public ResponseEntity<ResultDto<BillboardDto>> getBillboard(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);
        Billboard billboard = billboardService.findByMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new BillboardDto(billboard)));
    }

    @Operation(summary = "작가의 Billboard 수정하기")
    @PutMapping
    public ResponseEntity<ResultDto<BillboardDto>> updateBillboard(
            @RequestParam("aui") String aui,    // 현재 홈페이지 주인
            @Valid @RequestBody UpdateBillboardDto updateBillboardDto
    ){
        Member loginUser = authService.getMemberFromContext();
        //aui와 현재 로그인 한 Member가 같은 사람인지 확인
        //=> Security에서 처리할 수 있나?
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        Billboard updatedLb = billboardService.updateLb(
                updateBillboardDto.getId(),
                updateBillboardDto.getOriginUrl(),
                updateBillboardDto.getThumbnailUrl(),
                updateBillboardDto.getTitle(),
                updateBillboardDto.getDescription(),
                updateBillboardDto.getIsVisible()
                );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new BillboardDto(updatedLb)));
    }
}

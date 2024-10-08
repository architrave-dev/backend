package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.landingBox.request.UpdateLandingBoxDto;
import com.architrave.portfolio.api.dto.landingBox.response.LandingBoxDto;
import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.api.service.LandingBoxService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.LandingBox;
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

@Tag(name = "7. LandingBox")  // => swagger 이름
@Trace
@RestController
@RequestMapping("/api/v1/landing-box")
@RequiredArgsConstructor
public class LandingBoxController {

    private final LandingBoxService landingBoxService;
    private final MemberService memberService;
    private final AuthService authService;

    @Operation(
            summary = "작가의 LandingBox 조회하기",
            description = "LandingBox가 없을 경우 자동생성됩니다."
    )
    @GetMapping
    public ResponseEntity<ResultDto<LandingBoxDto>> getLandingBox(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);
        LandingBox landingBox = landingBoxService.findByMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new LandingBoxDto(landingBox)));
    }

    @Operation(summary = "작가의 LandingBox 수정하기")
    @PutMapping
    public ResponseEntity<ResultDto<LandingBoxDto>> updateLandingBox(
            @RequestParam("aui") String aui,    // 현재 홈페이지 주인
            @Valid @RequestBody UpdateLandingBoxDto updateLandingBoxDto
    ){
        Member loginUser = authService.getMemberFromContext();
        //aui와 현재 로그인 한 Member가 같은 사람인지 확인
        //=> Security에서 처리할 수 있나?
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        LandingBox updatedLb = landingBoxService.updateLb(
                updateLandingBoxDto.getId(),
                updateLandingBoxDto.getOriginUrl(),
                updateLandingBoxDto.getThumbnailUrl(),
                updateLandingBoxDto.getTitle(),
                updateLandingBoxDto.getDescription(),
                updateLandingBoxDto.getIsVisible()
                );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new LandingBoxDto(updatedLb)));
    }
}

package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.landingBox.request.UpdateLandingBoxDto;
import com.architrave.portfolio.api.dto.landingBox.response.LandingBoxDto;
import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.api.service.LandingBoxService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.LandingBox;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/landing_box")
@RequiredArgsConstructor
public class LandingBoxController {

    private final LandingBoxService landingBoxService;
    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/{aui}")
    public ResponseEntity<ResultDto<LandingBoxDto>> getLandingBox(
            @PathVariable("aui") String aui
    ){
        log.info("hello from getLandingBox");
        Member member = memberService.findMemberByAui(aui);
        LandingBox landingBox = member.getLandingBox();     //Transaction이 끝났는데 landingBox를 가져오려고 하니까 Exception

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new LandingBoxDto(landingBox)));
    }

    @PutMapping("/{aui}")
    public ResponseEntity<ResultDto<LandingBoxDto>> updateLandingBox(
            @RequestParam("aui") String aui,    // 현재 홈페이지 주인
            @RequestBody UpdateLandingBoxDto updateLandingBoxDto
    ){
        log.info("hello from updateLandingBox");
        Member loginUser = authService.getMemberFromContext();
        //aui와 현재 로그인 한 Member가 같은 사람인지 확인
        //=> Security에서 처리할 수 있나?
        if(loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }
        LandingBox landingBox = landingBoxService.updateLb(
                loginUser,
                updateLandingBoxDto.getOriginUrl(),
                updateLandingBoxDto.getThumbnailUrl(),
                updateLandingBoxDto.getTitle(),
                updateLandingBoxDto.getDescription(),
                updateLandingBoxDto.getIsDeleted()
                );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new LandingBoxDto(landingBox)));
    }
}

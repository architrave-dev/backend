package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.setting.request.UpdateSettingReq;
import com.architrave.portfolio.api.dto.setting.response.SettingDto;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.api.service.SettingService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Setting;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "12. Setting")  // => swagger 이름
@Slf4j
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/setting")
public class SettingController {

    private final MemberService memberService;
    private final SettingService settingService;
    private final OwnerContextHolder ownerContextHolder;

    @Operation(summary = "member로  Setting 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<SettingDto>> getSetting(
            @RequestParam("aui") String aui
    ){
        Member owner = memberService.findMemberByAui(aui);

        Setting setting = settingService.findSettingByMember(owner);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new SettingDto(setting, owner)));
    }


    @Operation(summary = "Setting 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<SettingDto>> updateSetting(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateSettingReq updateSettingReq
    ) {
        Member owner = ownerContextHolder.getOwner();

        Setting updated = settingService.updateSetting(
                updateSettingReq.getId(),
                updateSettingReq.getPageName(),
                updateSettingReq.getPageVisible(),
                updateSettingReq.getMenuVisible().getProjects(),
                updateSettingReq.getMenuVisible().getWorks(),
                updateSettingReq.getMenuVisible().getAbout(),
                updateSettingReq.getMenuVisible().getContact()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new SettingDto(updated, owner)));
    }
}

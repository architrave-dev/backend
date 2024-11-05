package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkPropertyVisibleReq;
import com.architrave.portfolio.api.dto.work.response.WorkPropertyVisibleDto;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.WorkPropertyVisible;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "06. WorkPropertyVisible")  // => swagger 이름
@Slf4j
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/work-property")
public class WorkPropertyVisibleController {

    private final AuthService authService;
    private final MemberService memberService;
    private final WorkPropertyVisibleService workPropertyVisibleService;

    @Operation(summary = "workDetailId로  WorkDetail 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<WorkPropertyVisibleDto>> getWorkPropertyVisible(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);
        WorkPropertyVisible workPropertyVisible = workPropertyVisibleService.findWPVByMember(member);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkPropertyVisibleDto(workPropertyVisible)));
    }


    @Operation(summary = "WorkDetail 수정하기")
    @PutMapping
    public ResponseEntity<ResultDto<WorkPropertyVisibleDto>> updateWorkDetail(
            @RequestParam("aui") String aui,
            @Valid @RequestBody UpdateWorkPropertyVisibleReq updateWorkPropertyVisibleReq
    ) {
        Member loginUser = authService.getMemberFromContext();
        if (!loginUser.getAui().equals(aui)) {
            throw new UnauthorizedException("loginUser is not page owner");
        }

        WorkPropertyVisible updated = workPropertyVisibleService.updateWorkPropertyVisible(
                updateWorkPropertyVisibleReq.getWorkPropertyVisibleId(),
                updateWorkPropertyVisibleReq.getWorkType(),
                updateWorkPropertyVisibleReq.getImageUrl(),
                updateWorkPropertyVisibleReq.getDescription(),
                updateWorkPropertyVisibleReq.getPrice(),
                updateWorkPropertyVisibleReq.getCollection()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkPropertyVisibleDto(updated)));
    }
}

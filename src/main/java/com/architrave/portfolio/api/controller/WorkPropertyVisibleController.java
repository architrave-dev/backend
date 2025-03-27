package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkPropertyVisibleReq;
import com.architrave.portfolio.api.dto.work.response.WorkPropertyVisibleDto;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.WorkPropertyVisible;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "07. WorkPropertyVisible")  // => swagger 이름
@Slf4j
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/work-property")
public class WorkPropertyVisibleController {
    private final MemberService memberService;
    private final WorkPropertyVisibleService workPropertyVisibleService;

    @Operation(summary = "WorkPropertyVisible 조회하기")
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


    @Operation(summary = "WorkPropertyVisible 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<WorkPropertyVisibleDto>> updateWorkPropertyVisible(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateWorkPropertyVisibleReq updateWorkPropertyVisibleReq
    ) {
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

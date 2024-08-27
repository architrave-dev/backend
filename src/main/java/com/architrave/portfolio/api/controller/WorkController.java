package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.work.request.CreateWorkReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.api.dto.work.response.WorkDto;
import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.api.service.WorkService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "5. Work")  // => swagger 이름
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/work")
public class WorkController {

    private final AuthService authService;
    private final MemberService memberService;
    private final WorkService workService;


    @Operation(summary = "작가의 Work List 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<List<WorkDto>>> getWorkListByMember(
            @RequestParam("aui") String aui
    ){
        log.info("hello from getWorkListByMember");

        Member member = memberService.findMemberByAui(aui);

        List<Work> workList = workService.findWorkByMember(member);
        log.info("workList.size(): ", workList.size());
        List<WorkDto> workDtoList = workList.stream()
                .map((w) -> new WorkDto(w))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(workDtoList));
    }

    @Operation(summary = "Work 생성하기")
    @PostMapping
    public ResponseEntity<ResultDto<WorkDto>> createWork(
            @RequestParam("aui") String aui,
            @Valid @RequestBody CreateWorkReq createWorkReq
    ){
        log.info("hello from createWork");
        Member loginUser = authService.getMemberFromContext();
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        Work createdWork = workService.createWork(
                loginUser,
                createWorkReq.getOriginUrl(),
                createWorkReq.getThumbnailUrl(),
                createWorkReq.getTitle(),
                createWorkReq.getDescription(),
                createWorkReq.getSize(),
                createWorkReq.getMaterial(),
                createWorkReq.getProdYear()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkDto(createdWork)));
    }

    @Operation(summary = "Work 수정하기")
    @PutMapping
    public ResponseEntity<ResultDto<WorkDto>> updateWork(
            @RequestParam("aui") String aui,
            @Valid @RequestBody UpdateWorkReq updateWorkReq
    ) {
        log.info("hello from updateWork");
        Member loginUser = authService.getMemberFromContext();
        if (!loginUser.getAui().equals(aui)) {
            throw new UnauthorizedException("loginUser is not page owner");
        }

        Work updatedWork = workService.updateWork(
                updateWorkReq.getId(),
                updateWorkReq.getOriginUrl(),
                updateWorkReq.getThumbnailUrl(),
                updateWorkReq.getTitle(),
                updateWorkReq.getDescription(),
                updateWorkReq.getSize(),
                updateWorkReq.getMaterial(),
                updateWorkReq.getProdYear(),
                updateWorkReq.getIsDeleted()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkDto(updatedWork)));
    }

    //delete 없음
}

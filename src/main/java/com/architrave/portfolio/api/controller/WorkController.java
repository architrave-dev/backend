package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.work.request.CreateWorkReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.api.dto.work.response.WorkDto;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.global.aop.Trace;
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
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/work")
public class WorkController {

    private final AuthService authService;
    private final MemberService memberService;
    private final WorkService workService;
    private final ProjectElementService peService;



    @Operation(summary = "작가의 Work List 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<List<WorkDto>>> getWorkListByMember(
            @RequestParam("aui") String aui
    ){
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
                updateWorkReq.getProdYear()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkDto(updatedWork)));
    }


    @Operation(summary = "Work 삭제하기",
            description = "Work 삭제 시 관련된 ProjectElement도 함께 삭제됩니다."
    )
    @DeleteMapping
    public ResponseEntity<ResultDto<String>> removeWork(
            @RequestParam("aui") String aui,
            @RequestParam("workId") Long targetId
    ) {
        Member loginUser = authService.getMemberFromContext();
        if (!loginUser.getAui().equals(aui)) {
            throw new UnauthorizedException("loginUser is not page owner");
        }

        Work work = workService.findWorkById(targetId);

        //삭제대상 work와 관련된 ProjectElement 삭제
        peService.deleteByMemberAndWorkId(loginUser, work);

        //work 삭제
        workService.removeWork(work);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("delete work success"));
    }
}

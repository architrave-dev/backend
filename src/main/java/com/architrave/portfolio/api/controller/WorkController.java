package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.work.request.CreateWorkReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkReq;
import com.architrave.portfolio.api.dto.work.response.WorkDetailDto;
import com.architrave.portfolio.api.dto.work.response.WorkDto;
import com.architrave.portfolio.api.dto.work.response.WorkSimpleDto;
import com.architrave.portfolio.api.dto.work.response.WorkWithDetailDto;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.WorkDetail;
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

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "05. Work")  // => swagger 이름
@Slf4j
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/work")
public class WorkController {
    private final MemberService memberService;
    private final WorkService workService;
    private final WorkDetailService workDetailService;
    private final ProjectElementService peService;
    private final OwnerContextHolder ownerContextHolder;


    @Operation(summary = "workId로 Work, 관련된 WorkDetail 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<WorkWithDetailDto>> getWorkAndDetailById(
            @RequestParam("workId") Long workId
    ){
        Work work = workService.findWorkById(workId);
        List<WorkDetail> workDetailList = workDetailService.findWorkDetailByWork(work);
        List<WorkDetailDto> workDetailDtoList = workDetailList.stream()
                .map((wd) -> new WorkDetailDto(wd))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkWithDetailDto(work, workDetailDtoList)));
    }
    @Operation(summary = "작가의 Work List 조회하기")
    @GetMapping("/list")
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
    @Operation(summary = "작가의 Work List 간단하게 조회하기",
    description = "getWorkListByMember보다 (2배 이상 빠르다) <br />" +
            "getWorkListByMember: test 시 평균 400ms (n+1 문제 있음)" +
            "getSimpleWorkListByMember: test 시 평균 200ms <br />"
    )
    @GetMapping("/list/simple")
    public ResponseEntity<ResultDto<List<WorkSimpleDto>>> getSimpleWorkListByMember(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);

        List<WorkSimpleDto> workSimpleList = workService.findSimpleWorkByMember(member);
        log.info("workList.size(): ", workSimpleList.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(workSimpleList));
    }

    @Operation(summary = "Work 생성하기")
    @PostMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<WorkDto>> createWork(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody CreateWorkReq createWorkReq
    ){
        Member owner = ownerContextHolder.getOwner();

        Work createdWork = workService.createWork(
                owner,
                createWorkReq.getWorkType(),
                createWorkReq.getOriginUrl(),
                createWorkReq.getThumbnailUrl(),
                createWorkReq.getTitle(),
                createWorkReq.getDescription(),
                createWorkReq.getSize(),
                createWorkReq.getMaterial(),
                createWorkReq.getProdYear(),
                createWorkReq.getPrice(),
                createWorkReq.getCollection()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkDto(createdWork)));
    }

    @Operation(summary = "Work 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<WorkDto>> updateWork(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateWorkReq updateWorkReq
    ) {
        Work updatedWork = workService.updateWork(
                updateWorkReq.getId(),
                updateWorkReq.getWorkType(),
                updateWorkReq.getUpdateUploadFileReq().getOriginUrl(),
                updateWorkReq.getUpdateUploadFileReq().getThumbnailUrl(),
                updateWorkReq.getTitle(),
                updateWorkReq.getDescription(),
                updateWorkReq.getSize(),
                updateWorkReq.getMaterial(),
                updateWorkReq.getProdYear(),
                updateWorkReq.getPrice(),
                updateWorkReq.getCollection()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkDto(updatedWork)));
    }


    @Operation(summary = "Work 삭제하기",
            description = "Work 삭제 시 관련된 ProjectElement도 함께 삭제됩니다."
    )
    @DeleteMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<String>> removeWork(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @RequestParam("workId") Long targetId
    ) {
        Member owner = ownerContextHolder.getOwner();

        Work work = workService.findWorkById(targetId);

        //Work와 관련된 WorkDetail 전부 삭제
        workDetailService.removeWorkDetailByWork(work);

        //삭제대상 work와 관련된 ProjectElement 삭제
        peService.deleteByMemberAndWorkId(owner, work);

        //work 삭제
        workService.removeWork(work);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("delete work success"));
    }
}

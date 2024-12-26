package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.work.request.CreateWorkDetailReq;
import com.architrave.portfolio.api.dto.work.request.UpdateWorkDetailReq;
import com.architrave.portfolio.api.dto.work.response.WorkDetailDto;
import com.architrave.portfolio.api.dto.work.response.WorkDetailSimpleDto;
import com.architrave.portfolio.api.service.*;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.WorkDetail;
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

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "07. WorkDetail")  // => swagger 이름
@Slf4j
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/work-detail")
public class WorkDetailController {
    private final WorkService workService;
    private final WorkDetailService workDetailService;

    @Operation(summary = "workDetailId로  WorkDetail 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<WorkDetailDto>> getWorkDetailById(
            @RequestParam("aui") String aui,
            @RequestParam("workDetailId") Long workDetailId
    ){

        WorkDetail workDetail = workDetailService.findWorkDetailById(workDetailId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkDetailDto(workDetail)));
    }

    @Operation(summary = "work로  WorkDetail 조회하기")
    @GetMapping("/list")
    public ResponseEntity<ResultDto<List<WorkDetailDto>>> getWorkDetailListByWork(
            @RequestParam("aui") String aui,
            @RequestParam("workId") Long workId
    ){

        Work work = workService.findWorkById(workId);

        List<WorkDetail> workDetailList = workDetailService.findWorkDetailByWork(work);

        List<WorkDetailDto> workDetailDtoList = workDetailList.stream()
                .map((wd) -> new WorkDetailDto(wd))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(workDetailDtoList));
    }
    @Operation(summary = "work로  WorkDetail 간단하게 조회하기")
    @GetMapping("/list/simple")
    public ResponseEntity<ResultDto<List<WorkDetailSimpleDto>>> getSimpleWorkDetailListByWork(
            @RequestParam("aui") String aui,
            @RequestParam("workId") Long workId
    ){
        Work work = workService.findWorkById(workId);

        List<WorkDetailSimpleDto> workDetailSimpleList = workDetailService.findSimpleWorkDetailByWork(work);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(workDetailSimpleList));
    }

    @Operation(summary = "WorkDetail 생성하기")
    @PostMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<WorkDetailDto>> createWork(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody CreateWorkDetailReq createWorkDetailReq
    ){
        Work work = workService.findWorkById(createWorkDetailReq.getWorkId());

        WorkDetail createdWorkDetail = workDetailService.createWorkDetail(
                work,
                createWorkDetailReq.getOriginUrl(),
                createWorkDetailReq.getThumbnailUrl(),
                createWorkDetailReq.getDescription()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkDetailDto(createdWorkDetail)));
    }

    @Operation(summary = "WorkDetail 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<WorkDetailDto>> updateWorkDetail(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateWorkDetailReq updateWorkDetailReq
    ) {
        WorkDetail updatedWorkDetail = workDetailService.updateWorkDetail(
                updateWorkDetailReq.getWorkDetailId(),
                updateWorkDetailReq.getUpdateUploadFileReq().getOriginUrl(),
                updateWorkDetailReq.getUpdateUploadFileReq().getThumbnailUrl(),
                updateWorkDetailReq.getDescription()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new WorkDetailDto(updatedWorkDetail)));
    }


    @Operation(summary = "WorkDetail 삭제하기"
            //description = "WorkDetail 삭제 시 관련된 ProjectElement도 함께 삭제됩니다."
            // => 아직 WorkDetail ProjectElement 지원 x
    )
    @DeleteMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<String>> removeWorkDetail(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @RequestParam("workDetailId") Long targetId
    ) {
        workDetailService.removeWorkDetailById(targetId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("delete work success"));
    }
}

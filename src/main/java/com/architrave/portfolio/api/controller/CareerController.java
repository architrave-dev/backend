package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.career.request.CreateCareerReq;
import com.architrave.portfolio.api.dto.career.request.UpdateCareerReq;
import com.architrave.portfolio.api.dto.career.response.CareerDto;
import com.architrave.portfolio.api.dto.reorder.request.ReorderReq;
import com.architrave.portfolio.api.dto.reorder.request.UpdateReorderListReq;
import com.architrave.portfolio.api.service.CareerService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck;
import com.architrave.portfolio.global.aop.ownerCheck.OwnerContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "09. Career")  // => swagger 이름
@Trace
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/career")
public class CareerController {

    private final CareerService careerService;

    private final MemberService memberService;
    private final OwnerContextHolder ownerContextHolder;

    @Operation(summary = "작가의 Career 조회하기")
    @GetMapping
    public ResponseEntity<ResultDto<List<CareerDto>>> getCareerList(
            @RequestParam("aui") String aui
    ){
        Member member = memberService.findMemberByAui(aui);
        List<Career> careerList= careerService.findCareerByMember(member);

        List<CareerDto> result = careerList.stream()
                .map((c) -> new CareerDto(c))
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(result));
    }
    @Operation(summary = "작가의 Career 생성하기")
    @PostMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<CareerDto>> createCareer(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody CreateCareerReq createCareerReq
    ){
        Member owner = ownerContextHolder.getOwner();

        Career created = careerService.createCareer(
                owner,
                createCareerReq.getCareerType(),
                createCareerReq.getContent(),
                createCareerReq.getYearFrom(),
                createCareerReq.getIndex()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new CareerDto(created)));
    }
    @Operation(summary = "작가의 Career 수정하기")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<CareerDto>> updateCareer(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateCareerReq updateCareerReq
    ){
        Career updated = careerService.updateCareer(
                updateCareerReq.getCareerId(),
                updateCareerReq.getContent(),
                updateCareerReq.getYearFrom()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(new CareerDto(updated)));
    }

    @Operation(summary = "Career 순서 수정하기")
    @PutMapping("/reorder")
    @OwnerCheck
    public ResponseEntity<ResultDto<List<CareerDto>>> reorderCareerList(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdateReorderListReq updateReorderListReq
    ){
        Member owner = ownerContextHolder.getOwner();
        CareerType careerType = CareerType.fromString(updateReorderListReq.getId());

        List<ReorderReq> reorderReqList = updateReorderListReq.getReorderReqList();

        List<Career> reorderedCareerList = careerService.reorder(owner, careerType, reorderReqList);

        List<CareerDto> result = reorderedCareerList.stream()
                .map((c) -> new CareerDto(c))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(result));
    }

    @Operation(summary = "작가의 Career 삭제하기")
    @DeleteMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<String>> deleteCareer(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @RequestParam("careerId") Long targetId
    ){
        careerService.removeCareerById(targetId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>("delete career success"));
    }
}

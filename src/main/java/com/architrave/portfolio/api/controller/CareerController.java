package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.career.request.CreateCareerReq;
import com.architrave.portfolio.api.dto.career.request.RemoveCareerReq;
import com.architrave.portfolio.api.dto.career.request.UpdateCareerReq;
import com.architrave.portfolio.api.dto.career.request.UpdatedCareerListReq;
import com.architrave.portfolio.api.dto.career.response.CareerDto;
import com.architrave.portfolio.api.service.CareerService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
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

@Tag(name = "08. Career")  // => swagger 이름
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

    @Operation(summary = "작가의 Career 수정하기",
            description = "한번의 요청으로 다음의 것들을 처리합니다. <br />" +
                    "1. 새롭게 추가되는 Career 리스트 <br />" +
                    "2. 기존 Career 변경 리스트 <br />" +
                    "3. 삭제되는 Career 리스트를 받습니다. ")
    @PutMapping
    @OwnerCheck
    public ResponseEntity<ResultDto<List<CareerDto>>> updateCareerList(
            @RequestParam("aui") String aui,    // aop OwnerCheck 에서 사용.
            @Valid @RequestBody UpdatedCareerListReq updatedCareerListReq
    ){
        Member owner = ownerContextHolder.getOwner();

        handleUpdateCareer(
                owner,
                updatedCareerListReq.getCreateCareerReqList(),
                updatedCareerListReq.getUpdateCareerReqList(),
                updatedCareerListReq.getRemoveCareerReqList()
        );

        List<Career> updatedCareerList = careerService.findCareerByMember(owner);
        List<CareerDto> result = updatedCareerList.stream()
                .map((c) -> new CareerDto(c))
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(result));
    }

    private void handleUpdateCareer(
            Member loginUser,
            List<CreateCareerReq> createCareerReqList,
            List<UpdateCareerReq> updateCareerReqList,
            List<RemoveCareerReq> removeCareerReqList
    ){
        //이러면 Transactional을 갯수만큼 열었다가 닫았다 하는거 아님...?
        createCareerReqList.stream()
                .forEach((c) -> careerService.createCareer(
                        loginUser,
                        c.getCareerType(),
                        c.getContent(),
                        c.getYearFrom()
                ));
        updateCareerReqList.stream()
                .forEach((c) -> careerService.updateCareer(
                        c.getCareerId(),
                        c.getContent(),
                        c.getYearFrom()
                ));
        removeCareerReqList.stream()
                .forEach((c) -> careerService.removeCareerById(c.getCareerId())
                );
    }
}

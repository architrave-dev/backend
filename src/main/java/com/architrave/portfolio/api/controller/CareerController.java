package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.api.dto.ResultDto;
import com.architrave.portfolio.api.dto.career.request.CreateCareerReq;
import com.architrave.portfolio.api.dto.career.request.RemoveCareerReq;
import com.architrave.portfolio.api.dto.career.request.UpdateCareerReq;
import com.architrave.portfolio.api.dto.career.request.UpdatedCareerListReq;
import com.architrave.portfolio.api.dto.career.response.CareerDto;
import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.api.service.CareerService;
import com.architrave.portfolio.api.service.MemberService;
import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/career")
public class CareerController {

    private final CareerService careerService;

    private final MemberService memberService;

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<ResultDto<List<CareerDto>>> getCareerList(
            @RequestParam("aui") String aui
    ){
        log.info("hello from getCareerList");
        Member member = memberService.findMemberByAui(aui);
        List<Career> careerList= careerService.findCareerByMember(member);

        List<CareerDto> result = careerList.stream()
                .map((c) -> new CareerDto(c))
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResultDto<>(result));
    }

    @PutMapping
    public ResponseEntity<ResultDto<List<CareerDto>>> updateCareerList(
            @RequestParam("aui") String aui,
            @Valid @RequestBody UpdatedCareerListReq updatedCareerListReq
    ){
        log.info("hello from updateCareerList");
        Member loginUser = authService.getMemberFromContext();
        if (!loginUser.getAui().equals(aui)) {
            throw new UnauthorizedException("loginUser is not page owner");
        }
        handleUpdateCareer(
                loginUser,
                updatedCareerListReq.getCreateCareerReqList(),
                updatedCareerListReq.getUpdateCareerReqList(),
                updatedCareerListReq.getRemoveCareerReqList()
        );

        List<Career> updatedCareerList = careerService.findCareerByMember(loginUser);
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
                        c.getYearFrom(),
                        c.getYearTo()
                ));
        updateCareerReqList.stream()
                .forEach((c) -> careerService.updateCareer(
                        c.getCareerId(),
                        c.getContent(),
                        c.getYearFrom(),
                        c.getYearTo()
                ));
        removeCareerReqList.stream()
                .forEach((c) -> careerService.removeCareerById(c.getCareerId())
                );
    }
}

package com.architrave.portfolio.api.controller;

import com.architrave.portfolio.global.aop.logTrace.Trace;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "00. AWS")  // => swagger 이름
@Trace
@RestController
@RequestMapping
public class HealthCheckController {

    @Operation(
            summary = "ALB의 health-check 처리",
            description = "ALB에서 target group에 health-check를 보낸다." +
                    "이에 200 응답을 리턴한다."
    )
    @GetMapping("/health-check")
    public ResponseEntity<Void> healthCheck(){
        return ResponseEntity.ok().build();
    }
}

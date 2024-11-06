package com.architrave.portfolio.global.aop.ownerCheck;

import com.architrave.portfolio.api.service.AuthService;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class OwnerCheckAspect {

    private final AuthService authService;
    private final OwnerContextHolder ownerContextHolder;

    @Pointcut("@annotation(com.architrave.portfolio.global.aop.ownerCheck.OwnerCheck)")
    public void methodLevel(){};

    /**
     * AUI 와 현재 로그인 한 Member가 같은 사람인지 확인
     * @param joinPoint, aui
     */
    @Before("methodLevel() && args(aui,..)")
    public void doOwnerCheck(JoinPoint joinPoint, String aui){
        String methodName = joinPoint.getSignature().getName();
        log.info("[before] Method: {}, aui: {}", methodName, aui);

        if (aui == null || aui.isEmpty()) {
            throw new RequiredValueEmptyException("aui required.");
        }

        Member loginUser = authService.getMemberFromContext();
        if(!loginUser.getAui().equals(aui)){
            throw new UnauthorizedException("loginUser is not page owner");
        }

        ownerContextHolder.setOwner(loginUser);
        log.info("website owner checked. : " + aui);
    }

    @After("methodLevel()")     //정상실행, 예외 시에도 동작한다.
    public void clearOwnerContext(JoinPoint joinPoint){
        log.info("clear ownerContext: {}", joinPoint.getSignature().getName());
        ownerContextHolder.clear();
    }
}

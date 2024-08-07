package com.architrave.portfolio.global.exception;

import com.architrave.portfolio.api.dto.ErrorDto;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            IllegalArgumentException.class,
            UsernameNotFoundException.class,
            NoSuchElementException.class
    })
    private ResponseEntity<ErrorDto> illgalArgumentExceptionHandler(RuntimeException e){
        log.info("handle in ExControllerAdvice: ", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(e.getMessage(), LocalDateTime.now()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class, UnauthorizedException.class})
    private ResponseEntity<ErrorDto> authenticationExceptionExceptionHandler(
            AuthenticationException e
    ){
        log.info("handle in ExControllerAdvice: ", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(e.getMessage(), LocalDateTime.now()));
    }
}

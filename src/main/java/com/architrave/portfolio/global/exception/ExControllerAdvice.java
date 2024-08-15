package com.architrave.portfolio.global.exception;

import com.architrave.portfolio.api.dto.ErrorDto;
import com.architrave.portfolio.domain.model.enumType.ErrorCode;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorDto> dtoExceptionHandler(MethodArgumentNotValidException e){
        log.info("handle in ExControllerAdvice: ", e);
        StringBuilder message = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            FieldError fieldErr = ((FieldError) error);
            String fieldName = fieldErr.getField();
            if(!message.isEmpty()){
                message.append(", ");
            }
            message.append(fieldName);

        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ErrorCode.IDF, "Please check this fieldName: " + message.toString()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            UsernameNotFoundException.class,
            NoSuchElementException.class
    })
    private ResponseEntity<ErrorDto> NotFoundResultExceptionHandler(RuntimeException e){
        log.info("handle in ExControllerAdvice: ", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto( ErrorCode.NFR, e.getMessage()));
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            IllegalArgumentException.class,
    })
    private ResponseEntity<ErrorDto> illegalArgumentExceptionHandler(IllegalArgumentException e){
        log.info("handle in ExControllerAdvice: ", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto( ErrorCode.AEV, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            RequiredValueEmptyException.class,
    })
    private ResponseEntity<ErrorDto> requiredValueEmptyExceptionHandler(RequiredValueEmptyException e){
        log.info("handle in ExControllerAdvice: ", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto( ErrorCode.RVN, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({BadCredentialsException.class, UnauthorizedException.class})
    private ResponseEntity<ErrorDto> authenticationExceptionExceptionHandler(
            AuthenticationException e
    ){
        log.info("handle in ExControllerAdvice: ", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(ErrorCode.NAU, e.getMessage()));
    }
}

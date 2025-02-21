package com.architrave.portfolio.global.exception;

import com.architrave.portfolio.api.dto.ErrorDto;
import com.architrave.portfolio.domain.model.enumType.ErrorCode;
import com.architrave.portfolio.global.exception.custom.ExpiredTokenException;
import com.architrave.portfolio.global.exception.custom.InvalidTokenException;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import com.architrave.portfolio.global.exception.custom.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
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
import software.amazon.awssdk.services.ses.model.MessageRejectedException;
import software.amazon.awssdk.services.ses.model.SesException;

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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ExpiredTokenException.class, InvalidTokenException.class})
    private ResponseEntity<ErrorDto> expiredExceptionHandler(
            ExpiredTokenException e
    ){
        log.info("handle in ExControllerAdvice: ", e);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(ErrorCode.RTX, e.getMessage()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<ErrorDto> handlePSQLException(PSQLException e) {
        log.info("handle in ExControllerAdvice: ", e);
        // PostgreSQL unique constraint 위반은 SQLState = 23505
        if ("23505".equals(e.getSQLState())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorDto(ErrorCode.DUK, "이미 존재하는 값입니다."));
        } else {
            // 그 외 PSQLException의 경우
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDto(ErrorCode.DBE, "데이터베이스 처리 중 오류가 발생했습니다."));
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SesException.class, MessageRejectedException.class})
    private ResponseEntity<ErrorDto> emailSendExceptionHandler(
            SesException e
    ){
        log.info("handle in ExControllerAdvice: ", e);

        String awsErrorCode    = e.awsErrorDetails().errorCode();    // 예: "MessageRejected"
        String awsErrorMessage = e.awsErrorDetails().errorMessage(); // 예: "Email address is not verified."

        HttpStatus status;
        ErrorCode errorCode;
        switch (awsErrorCode) {
            case "MessageRejected":
                // 예: 검증되지 않은 이메일, 포맷 문제 등
                status = HttpStatus.BAD_REQUEST;
                errorCode = ErrorCode.EVF;
                break;
            case "Throttling":
                // 발송량 초과(SES 할당량)
                status = HttpStatus.TOO_MANY_REQUESTS;
                errorCode = ErrorCode.EME;
                break;
            default:
                // 그 외 SES 오류
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errorCode = ErrorCode.EME;
                break;
        }

        return ResponseEntity
                .status(status)
                .body(new ErrorDto(errorCode, awsErrorMessage));
    }
}

package com.architrave.portfolio.api.dto;


import com.architrave.portfolio.domain.model.enumType.ErrorCode;

import java.time.LocalDateTime;

public record ErrorDto(ErrorCode errorCode, String message, LocalDateTime timestamp) {
    public ErrorDto( ErrorCode errorCode, String message) {
        this(errorCode, message, LocalDateTime.now());
    }
}

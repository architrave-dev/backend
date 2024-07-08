package com.architrave.portfolio.api.dto;


import java.time.LocalDateTime;

public record ErrorDto(String message, LocalDateTime timestamp) {
}

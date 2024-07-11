package com.architrave.portfolio.global.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnauthorizedException extends RuntimeException{

    private String message;
}

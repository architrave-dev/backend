package com.architrave.portfolio.global.exception.custom;

import com.architrave.portfolio.domain.model.enumType.ErrorCode;
import lombok.Getter;

@Getter
public class RequiredValueEmptyException extends IllegalArgumentException{
    private final ErrorCode errorCode = ErrorCode.RVN;
    public RequiredValueEmptyException(String msg, Throwable cause){ super( msg, cause);}

    public RequiredValueEmptyException(String msg)  {
        super(msg);
    }
}

package com.alom.reeltalkbe.global;

import com.alom.reeltalkbe.common.exception.BaseException;
import com.alom.reeltalkbe.common.response.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * BaseException 예외 처리
     */
    @ExceptionHandler(BaseException.class)
    public BaseResponse<String> handleBaseException(BaseException ex) {
        return new BaseResponse<>(ex.getStatus());
    }

}

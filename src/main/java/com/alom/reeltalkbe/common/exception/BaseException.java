package com.alom.reeltalkbe.common.exception;


import com.alom.reeltalkbe.common.response.BaseResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {
    private BaseResponseStatus status;

    /**
     * throw new BaseException(BaseResponseStatus.DATABASE_INSERT_ERROR)와 같이 사용.
     */
    public BaseException(BaseResponseStatus status) {
        super(status.getMessage());
        this.status = status;
    }
}


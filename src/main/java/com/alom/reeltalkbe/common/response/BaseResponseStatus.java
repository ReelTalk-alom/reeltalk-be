package com.alom.reeltalkbe.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseResponseStatus {
    /**
     * 성공 코드 2xx
     * 코드의 원활한 이해을 위해 code는 숫자가 아닌 아래 형태로 입력해주세요.
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),

    // 4xx : client error
    FAIL_TOKEN_AUTHORIZATION(false, HttpStatus.UNAUTHORIZED.value(), "토큰 인증에 실패하였습니다."),
    NO_TOKEN(false, HttpStatus.NO_CONTENT.value(), "토큰 값이 비어있습니다."),

    EXIST_EMAIL(false, HttpStatus.CONFLICT.value(), "이미 존재하는 이메일입니다."),
    EXIST_USERNAME(false, HttpStatus.CONFLICT.value(), "이미 존재하는 사용자 이름입니다."),
    EXIST_RATING(false, HttpStatus.CONFLICT.value(), "이미 평가된 컨텐츠입니다."),


    NON_EXIST_USER(false, HttpStatus.UNAUTHORIZED.value(), "존재하지 않는 회원입니다."),
    HTTP_METHOD_ERROR(false, HttpStatus.FORBIDDEN.value(), "http 메서드가 올바르지 않습니다."),

    INVALID_MEMBER(false, HttpStatus.NOT_FOUND.value(), "유효하지 않은 회원입니다."),
    INVALID_REVIEW(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 리뷰입니다."),
    INVALID_MEETING(false, HttpStatus.NO_CONTENT.value(), "존재하지 않는 게시글입니다."),
    INVALID_CHATROOM(false, HttpStatus.NOT_FOUND.value(), "유효하지 않은 채팅방입니다."),

    CONTENT_NOT_FOUND(false, HttpStatus.NO_CONTENT.value(), "존재하지 않는 컨텐츠입니다."),
    INVALID_REQUEST(false, HttpStatus.NO_CONTENT.value(), "유효하지 않은 요청입니다."),

    INVALID_QUESTION(false, HttpStatus.NO_CONTENT.value(), "유효하지 않은 질문입니다."),
    INVALID_ANSWER(false, HttpStatus.NO_CONTENT.value(), "답변을 입력해주세요."),
    ANSWER_NOT_FOUND(false, HttpStatus.NO_CONTENT.value(), "답변이 존재하지 않습니다."),
    COMMENT_NOT_FOUND(false, HttpStatus.NO_CONTENT.value(), "댓글이 존재하지 않습니다."),

    // 5xx : server error
    FAIL_REVIEW_POST(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "리뷰 작성에 실패했습니다."),
    DATABASE_INSERT_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 입력에 실패했습니다."),
    FAIL_IMAGE_CONVERT(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Multipart 파일 전환에 실패했습니다.");

    private final boolean isSuccess;
    private final int code;
    private final String message;

    /**
     * isSuccess : 요청의 성공 또는 실패
     * code : Http Status Code
     * message : 설명
     */

    BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}

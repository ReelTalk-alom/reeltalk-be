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

    // 인증
    FAIL_TOKEN_AUTHORIZATION(false, HttpStatus.UNAUTHORIZED.value(), "토큰 인증에 실패하였습니다."),
    NO_TOKEN(false, HttpStatus.NO_CONTENT.value(), "토큰 값이 비어있습니다."),
    INVALID_MEMBER(false, HttpStatus.NOT_FOUND.value(), "유효하지 않은 회원입니다."),

    // 회원 가입
    EXIST_EMAIL(false, HttpStatus.CONFLICT.value(), "이미 존재하는 이메일입니다."),
    EXIST_USERNAME(false, HttpStatus.CONFLICT.value(), "이미 존재하는 사용자 이름입니다."),

    // 로그인
    NON_EXIST_USER(false, HttpStatus.UNAUTHORIZED.value(), "존재하지 않는 회원입니다."),
    HTTP_METHOD_ERROR(false, HttpStatus.FORBIDDEN.value(), "http 메서드가 올바르지 않습니다."),

    // 실시간 톡
    NOT_YOUR_MESSAGE(false, HttpStatus.NO_CONTENT.value(), "다른 유저의 톡 메시지입니다."),
    MESSAGE_NOT_FOUND(false, HttpStatus.NO_CONTENT.value(), "존재하지 않는 실시간 톡 메시지입니다."),

    // 컨텐츠
    CONTENT_NOT_FOUND(false, HttpStatus.NO_CONTENT.value(), "존재하지 않는 컨텐츠입니다."),
    RATING_NOT_FOUND(false, HttpStatus.NO_CONTENT.value(), "아직 평가하지 않은 컨텐츠입니다."),
    EXIST_RATING(false, HttpStatus.CONFLICT.value(), "이미 평가된 컨텐츠입니다."),
    INVALID_QUERY_PARAMETER(false, HttpStatus.NO_CONTENT.value(), "유효하지 않은 분류 기준입니다."),


    // 리뷰
    INVALID_REVIEW(false, HttpStatus.NOT_FOUND.value(), "존재하지 않는 리뷰입니다."),

    // 댓글
    COMMENT_NOT_FOUND(false, HttpStatus.NO_CONTENT.value(), "댓글이 존재하지 않습니다."),
    INVALID_COMMENT(false, HttpStatus.NOT_FOUND.value(), "존재하지 않은 댓글입니다."),

    // JSON 처리 오류
    JSON_CONVERT_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "JSON 변환 중 오류가 발생했습니다."),
    JSON_PARSE_ERROR(false, HttpStatus.BAD_REQUEST.value(), "JSON 파싱 중 오류가 발생했습니다."),

    // 이미지 처리 오류
    IMAGE_UPLOAD_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 업로드 중 오류가 발생했습니다."),
    IMAGE_CONVERT_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 변환에 실패했습니다."),
    IMAGE_DELETE_FAILED(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미지 삭제 중 오류가 발생했습니다."),
    FILE_NOT_FOUND(false, HttpStatus.NOT_FOUND.value(), "삭제하려는 파일이 존재하지 않습니다."),
    INVALID_IMAGE_FORMAT(false, HttpStatus.BAD_REQUEST.value(), "올바르지 않은 이미지 형식입니다."),

    // 5xx : server error
    FAIL_REVIEW_POST(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "리뷰 작성에 실패했습니다."),
    FAIL_COMMENT_POST(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "댓글 작성에 실패했습니다."),
    FAIL_COMMENT_DELETE(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "댓글 삭제에 실패했습니다."),
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
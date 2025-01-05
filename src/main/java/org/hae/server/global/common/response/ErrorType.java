package org.hae.server.global.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorType {

    /**
     * 400 BAD REQUEST (4000 ~ 4099)
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 4000, "잘못된 요청입니다."),
    REQUEST_VALIDATION(HttpStatus.BAD_REQUEST, 4001, "잘못된 요청입니다."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, 4002, "잘못된 타입이 입력되었습니다."),
    INVALID_MISSING_HEADER(HttpStatus.BAD_REQUEST, 4003, "요청에 필요한 헤더값이 존재하지 않습니다."),
    INVALID_HTTP_REQUEST(HttpStatus.BAD_REQUEST, 4004, "허용되지 않는 문자열이 입력되었습니다."),
    /**
     * 401 UNAUTHROZIED (4100 ~ 4199)
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 4100, "인증되지 않았습니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, 4109, "알 수 없는 이유로 요청이 거절되었습니다."),


    /**
     * 403 FORBIDDEN (4300 ~ 4399)
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, 4300, "해당 자원에 접근 권한이 없습니다."),
    FORBIDDEN_USER(HttpStatus.FORBIDDEN, 4301, "사용자 접근 권한이 없습니다"),
    FORBIDDEN_USER_MODIFY(HttpStatus.FORBIDDEN, 4302, "사용자 수정 권한이 없습니다"),

    /**
     * 404 NOT FOUND (4400 ~ 4499)
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, 4400, "존재하지 않는 리소스입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, 4401, "존재하지 않는 유저입니다."),

    /**
     * 405 Method Not Allowed (4500 ~ 4599)
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 4500, "잘못된 HTTP method 요청입니다."),

    /**
     * 500 INTERNAL SERVER (5000 ~ 5099)
     */
    INTERNAL_SERVER(HttpStatus.INTERNAL_SERVER_ERROR, 5000, "알 수 없는 서버 에러가 발생했습니다");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}
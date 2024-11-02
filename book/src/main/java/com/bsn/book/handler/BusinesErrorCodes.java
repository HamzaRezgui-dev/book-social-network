package com.bsn.book.handler;

import org.springframework.http.HttpStatus;

import lombok.Getter;


public enum BusinesErrorCodes {

    NO_CODE(0, HttpStatus.NOT_IMPLEMENTED,"No Code" ),
    INCORRECT_CURRENT_PASSWORD(300, HttpStatus.BAD_REQUEST, "Incorrect current password"),
    NEW_PASSWORD_DOES_NOT_MATCH(301, HttpStatus.BAD_REQUEST, "New password does not match"),
    ACCOUNT_LOCKED(302, HttpStatus.FORBIDDEN, "Account locked"),
    ACCOUNT_DISABLED(303, HttpStatus.FORBIDDEN, "Account disabled"),
    BAD_CREDENTIALS(304, HttpStatus.UNAUTHORIZED, "email and / or password is incorrect"),
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinesErrorCodes(Integer code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
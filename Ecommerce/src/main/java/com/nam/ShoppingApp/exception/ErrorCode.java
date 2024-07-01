package com.nam.ShoppingApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    USER_EXISTED(100, "User already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(102, "User not existed", HttpStatus.NOT_FOUND),
    UNCATEGORIZED_EXCEPTION(101, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(104, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    USERNAME_INVALID(102, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(103, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    NOT_FOUND(404, "Not found", HttpStatus.NOT_FOUND),
    UNAUTHORIZED(401, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(400, "You must be at least {min}", HttpStatus.BAD_REQUEST),
    ORDER_NOT_FOUND(404, "Order not found", HttpStatus.NOT_FOUND),
    INVALID_ORDER_STATUS(400, "Invalid order status", HttpStatus.BAD_REQUEST),
    COUPON_ALREADY_EXISTS(400, "Coupon already exists", HttpStatus.BAD_REQUEST),
    CUSTOMER_COUPON_EXPIRED(400, "Coupon is expired", HttpStatus.BAD_REQUEST),
    COUPON_NOT_FOUND(404, "Coupon not found", HttpStatus.NOT_FOUND),;


    private int code;
    private String message;
    private HttpStatusCode statusCode;

     ErrorCode(int code, String message, HttpStatusCode statusCode){
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getCode(){
        return code;
    }

    public String getMessage(){
        return message;
    }

    public HttpStatusCode getStatusCode(){
        return statusCode;
    }

}

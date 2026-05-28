package com.challenge.coupon.domain.exception;

public class CouponInvalidCodeException extends RuntimeException {
    public CouponInvalidCodeException(String message) {
        super(message);
    }
}

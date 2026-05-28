package com.challenge.coupon.domain.exception;

public class CouponExpiredDateException extends RuntimeException {
    public CouponExpiredDateException(String message) {
        super(message);
    }
}

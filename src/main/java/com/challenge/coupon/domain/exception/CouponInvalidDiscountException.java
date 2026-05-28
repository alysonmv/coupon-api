package com.challenge.coupon.domain.exception;

public class CouponInvalidDiscountException extends RuntimeException {
    public CouponInvalidDiscountException(String message) {
        super(message);
    }
}

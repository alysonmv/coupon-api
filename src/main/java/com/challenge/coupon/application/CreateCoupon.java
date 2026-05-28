package com.challenge.coupon.application;

import com.challenge.coupon.domain.Coupon;
import com.challenge.coupon.domain.CouponCode;
import com.challenge.coupon.domain.CouponRepositoryPort;

import java.time.LocalDateTime;

public class CreateCoupon {

    private final CouponRepositoryPort repository;

    public CreateCoupon(CouponRepositoryPort repository) {
        this.repository = repository;
    }

    public Coupon execute(String code, String description, double discountValue,
                          LocalDateTime expirationDate, boolean published) {
        CouponCode couponCode = new CouponCode(code);
        Coupon coupon = new Coupon(couponCode, description, discountValue, expirationDate, published);
        return repository.save(coupon);
    }
}

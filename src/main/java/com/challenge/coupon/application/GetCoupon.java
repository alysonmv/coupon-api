package com.challenge.coupon.application;

import com.challenge.coupon.domain.Coupon;
import com.challenge.coupon.domain.CouponRepositoryPort;
import com.challenge.coupon.domain.exception.CouponNotFoundException;

import java.util.UUID;

public class GetCoupon {

    private final CouponRepositoryPort repository;

    public GetCoupon(CouponRepositoryPort repository) {
        this.repository = repository;
    }

    public Coupon execute(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));
    }
}

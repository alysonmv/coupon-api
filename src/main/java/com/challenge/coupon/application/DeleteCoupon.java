package com.challenge.coupon.application;

import com.challenge.coupon.domain.Coupon;
import com.challenge.coupon.domain.CouponRepositoryPort;
import com.challenge.coupon.domain.exception.CouponNotFoundException;

import java.util.UUID;

public class DeleteCoupon {

    private final CouponRepositoryPort repository;

    public DeleteCoupon(CouponRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(UUID id) {
        Coupon coupon = repository.findById(id)
            .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));
        coupon.softDelete();
        repository.save(coupon);
    }
}

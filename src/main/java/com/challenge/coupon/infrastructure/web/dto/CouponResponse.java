package com.challenge.coupon.infrastructure.web.dto;

import com.challenge.coupon.domain.Coupon;
import com.challenge.coupon.domain.CouponStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record CouponResponse(
    UUID id,
    String code,
    String description,
    double discountValue,
    LocalDateTime expirationDate,
    CouponStatus status,
    boolean published,
    boolean redeemed
) {
    public static CouponResponse from(Coupon coupon) {
        return new CouponResponse(
            coupon.getId(),
            coupon.getCode().getValue(),
            coupon.getDescription(),
            coupon.getDiscountValue(),
            coupon.getExpirationDate(),
            coupon.getStatus(),
            coupon.isPublished(),
            coupon.isRedeemed()
        );
    }
}

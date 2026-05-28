package com.challenge.coupon.domain;

import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
import com.challenge.coupon.domain.exception.CouponExpiredDateException;
import com.challenge.coupon.domain.exception.CouponInvalidDiscountException;

import java.time.LocalDateTime;
import java.util.UUID;

public class Coupon {

    private final UUID id;
    private final CouponCode code;
    private final String description;
    private final double discountValue;
    private final LocalDateTime expirationDate;
    private CouponStatus status;
    private final boolean published;
    private boolean redeemed;

    public Coupon(CouponCode code, String description, double discountValue,
                  LocalDateTime expirationDate, boolean published) {
        if (discountValue < 0.5) {
            throw new CouponInvalidDiscountException(
                "Discount value must be at least 0.5, got: " + discountValue
            );
        }
        if (expirationDate.isBefore(LocalDateTime.now())) {
            throw new CouponExpiredDateException(
                "Expiration date must not be in the past"
            );
        }
        this.id = UUID.randomUUID();
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = CouponStatus.ACTIVE;
        this.published = published;
        this.redeemed = false;
    }

    public Coupon(UUID id, CouponCode code, String description, double discountValue,
                  LocalDateTime expirationDate, CouponStatus status, boolean published, boolean redeemed) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountValue = discountValue;
        this.expirationDate = expirationDate;
        this.status = status;
        this.published = published;
        this.redeemed = redeemed;
    }

    public void softDelete() {
        if (this.status == CouponStatus.DELETED) {
            throw new CouponAlreadyDeletedException(
                "Coupon is already deleted"
            );
        }
        this.status = CouponStatus.DELETED;
    }

    public UUID getId() { return id; }
    public CouponCode getCode() { return code; }
    public String getDescription() { return description; }
    public double getDiscountValue() { return discountValue; }
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public CouponStatus getStatus() { return status; }
    public boolean isPublished() { return published; }
    public boolean isRedeemed() { return redeemed; }
}

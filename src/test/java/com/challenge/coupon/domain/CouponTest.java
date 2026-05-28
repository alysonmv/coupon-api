package com.challenge.coupon.domain;

import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
import com.challenge.coupon.domain.exception.CouponExpiredDateException;
import com.challenge.coupon.domain.exception.CouponInvalidDiscountException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {

    private final LocalDateTime futureDate = LocalDateTime.now().plusDays(30);
    private final CouponCode validCode = new CouponCode("ABC123");

    @Test
    void shouldCreateValidCouponWithPublishedFalse() {
        Coupon coupon = new Coupon(validCode, "Test coupon", 1.0, futureDate, false);
        assertEquals("ABC123", coupon.getCode().getValue());
        assertEquals(CouponStatus.ACTIVE, coupon.getStatus());
        assertFalse(coupon.isPublished());
        assertFalse(coupon.isRedeemed());
    }

    @Test
    void shouldCreateValidCouponWithPublishedTrue() {
        Coupon coupon = new Coupon(validCode, "Test coupon", 1.0, futureDate, true);
        assertTrue(coupon.isPublished());
    }

    @Test
    void shouldThrowWhenDiscountValueBelowMinimum() {
        assertThrows(CouponInvalidDiscountException.class,
            () -> new Coupon(validCode, "desc", 0.4, futureDate, false));
    }

    @Test
    void shouldAcceptMinimumDiscountValue() {
        assertDoesNotThrow(() -> new Coupon(validCode, "desc", 0.5, futureDate, false));
    }

    @Test
    void shouldThrowWhenExpirationDateIsInThePast() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        assertThrows(CouponExpiredDateException.class,
            () -> new Coupon(validCode, "desc", 1.0, pastDate, false));
    }

    @Test
    void shouldSoftDeleteAndChangeStatusToDeleted() {
        Coupon coupon = new Coupon(validCode, "desc", 1.0, futureDate, false);
        coupon.softDelete();
        assertEquals(CouponStatus.DELETED, coupon.getStatus());
    }

    @Test
    void shouldThrowWhenSoftDeletingAlreadyDeletedCoupon() {
        Coupon coupon = new Coupon(validCode, "desc", 1.0, futureDate, false);
        coupon.softDelete();
        assertThrows(CouponAlreadyDeletedException.class, coupon::softDelete);
    }
}

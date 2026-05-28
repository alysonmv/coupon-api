package com.challenge.coupon.application;

import com.challenge.coupon.domain.Coupon;
import com.challenge.coupon.domain.CouponCode;
import com.challenge.coupon.domain.CouponRepositoryPort;
import com.challenge.coupon.domain.exception.CouponExpiredDateException;
import com.challenge.coupon.domain.exception.CouponInvalidCodeException;
import com.challenge.coupon.domain.exception.CouponInvalidDiscountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCouponTest {

    @Mock
    private CouponRepositoryPort repository;

    private CreateCoupon createCoupon;

    private final LocalDateTime futureDate = LocalDateTime.now().plusDays(30);

    @BeforeEach
    void setUp() {
        createCoupon = new CreateCoupon(repository);
    }

    @Test
    void shouldCreateCouponSuccessfullyWithSanitizedCode() {
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Coupon result = createCoupon.execute("ABC-123", "desc", 1.0, futureDate, false);

        assertEquals("ABC123", result.getCode().getValue());
        assertEquals("desc", result.getDescription());
    }

    @Test
    void shouldThrowWhenCodeIsInvalidAfterSanitization() {
        assertThrows(CouponInvalidCodeException.class,
            () -> createCoupon.execute("AB-1", "desc", 1.0, futureDate, false));
    }

    @Test
    void shouldThrowWhenDiscountValueIsInvalid() {
        assertThrows(CouponInvalidDiscountException.class,
            () -> createCoupon.execute("ABC123", "desc", 0.3, futureDate, false));
    }

    @Test
    void shouldThrowWhenExpirationDateIsInThePast() {
        LocalDateTime pastDate = LocalDateTime.now().minusDays(1);
        assertThrows(CouponExpiredDateException.class,
            () -> createCoupon.execute("ABC123", "desc", 1.0, pastDate, false));
    }
}

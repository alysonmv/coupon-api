package com.challenge.coupon.application;

import com.challenge.coupon.domain.Coupon;
import com.challenge.coupon.domain.CouponCode;
import com.challenge.coupon.domain.CouponRepositoryPort;
import com.challenge.coupon.domain.exception.CouponNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCouponTest {

    @Mock
    private CouponRepositoryPort repository;

    private GetCoupon getCoupon;

    @BeforeEach
    void setUp() {
        getCoupon = new GetCoupon(repository);
    }

    @Test
    void shouldReturnCouponWhenFound() {
        Coupon coupon = new Coupon(
            new CouponCode("ABC123"), "desc", 1.0, LocalDateTime.now().plusDays(30), false
        );
        when(repository.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        Coupon result = getCoupon.execute(coupon.getId());

        assertEquals(coupon.getId(), result.getId());
    }

    @Test
    void shouldThrowWhenCouponNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> getCoupon.execute(id));
    }
}

package com.challenge.coupon.application;

import com.challenge.coupon.domain.Coupon;
import com.challenge.coupon.domain.CouponCode;
import com.challenge.coupon.domain.CouponRepositoryPort;
import com.challenge.coupon.domain.CouponStatus;
import com.challenge.coupon.domain.exception.CouponAlreadyDeletedException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCouponTest {

    @Mock
    private CouponRepositoryPort repository;

    private DeleteCoupon deleteCoupon;

    @BeforeEach
    void setUp() {
        deleteCoupon = new DeleteCoupon(repository);
    }

    @Test
    void shouldSoftDeleteCouponSuccessfully() {
        Coupon coupon = new Coupon(
            new CouponCode("ABC123"), "desc", 1.0, LocalDateTime.now().plusDays(30), false
        );
        when(repository.findById(coupon.getId())).thenReturn(Optional.of(coupon));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        deleteCoupon.execute(coupon.getId());

        assertEquals(CouponStatus.DELETED, coupon.getStatus());
        verify(repository).save(coupon);
    }

    @Test
    void shouldThrowWhenCouponNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(CouponNotFoundException.class, () -> deleteCoupon.execute(id));
    }

    @Test
    void shouldThrowWhenCouponIsAlreadyDeleted() {
        Coupon coupon = new Coupon(
            new CouponCode("ABC123"), "desc", 1.0, LocalDateTime.now().plusDays(30), false
        );
        coupon.softDelete();
        when(repository.findById(coupon.getId())).thenReturn(Optional.of(coupon));

        assertThrows(CouponAlreadyDeletedException.class, () -> deleteCoupon.execute(coupon.getId()));
    }
}

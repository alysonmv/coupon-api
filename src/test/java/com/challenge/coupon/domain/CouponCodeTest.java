package com.challenge.coupon.domain;

import com.challenge.coupon.domain.exception.CouponInvalidCodeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponCodeTest {

    @Test
    void shouldSanitizeAndAcceptValidCode() {
        CouponCode code = new CouponCode("ABC-123");
        assertEquals("ABC123", code.getValue());
    }

    @Test
    void shouldTruncateToSixCharsWhenExceedsLimit() {
        CouponCode code = new CouponCode("ABC-1234567");
        assertEquals("ABC123", code.getValue());
    }

    @Test
    void shouldRemoveSpecialCharsAndAccept() {
        CouponCode code = new CouponCode("A!B@C#1$2%3");
        assertEquals("ABC123", code.getValue());
    }

    @Test
    void shouldThrowWhenSanitizedResultHasLessThanSixChars() {
        assertThrows(CouponInvalidCodeException.class, () -> new CouponCode("ABC-1"));
    }

    @Test
    void shouldThrowWhenOnlySpecialCharsProduceLessThanSix() {
        assertThrows(CouponInvalidCodeException.class, () -> new CouponCode("A!B@C"));
    }

    @Test
    void shouldThrowWhenCodeIsNull() {
        assertThrows(CouponInvalidCodeException.class, () -> new CouponCode(null));
    }

    @Test
    void shouldThrowWhenCodeIsBlank() {
        assertThrows(CouponInvalidCodeException.class, () -> new CouponCode("   "));
    }
}

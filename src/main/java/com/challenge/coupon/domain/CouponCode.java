package com.challenge.coupon.domain;

import com.challenge.coupon.domain.exception.CouponInvalidCodeException;

public final class CouponCode {

    private final String value;

    public CouponCode(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new CouponInvalidCodeException("Code must not be blank");
        }
        String sanitized = raw.replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        if (sanitized.length() < 6) {
            throw new CouponInvalidCodeException(
                "Code must contain at least 6 alphanumeric characters after sanitization, got: " + sanitized.length()
            );
        }
        this.value = sanitized.substring(0, 6);
    }

    private CouponCode(String value, boolean reconstitute) {
        this.value = value;
    }

    public static CouponCode reconstitute(String value) {
        return new CouponCode(value, true);
    }

    public String getValue() {
        return value;
    }
}

package com.challenge.coupon.infrastructure.web.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateCouponRequest(
    @NotBlank(message = "Code must not be blank")
    String code,

    @NotBlank(message = "Description must not be blank")
    String description,

    @NotNull(message = "Discount value must not be null")
    @DecimalMin(value = "0.5", message = "Discount value must be at least 0.5")
    Double discountValue,

    @NotNull(message = "Expiration date must not be null")
    LocalDateTime expirationDate,

    boolean published
) {}

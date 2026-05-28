package com.challenge.coupon.infrastructure.web;

import com.challenge.coupon.application.CreateCoupon;
import com.challenge.coupon.application.DeleteCoupon;
import com.challenge.coupon.application.GetCoupon;
import com.challenge.coupon.domain.Coupon;
import com.challenge.coupon.infrastructure.web.dto.CouponResponse;
import com.challenge.coupon.infrastructure.web.dto.CreateCouponRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@Tag(name = "Coupon")
public class CouponController {

    private final CreateCoupon createCoupon;
    private final GetCoupon getCoupon;
    private final DeleteCoupon deleteCoupon;

    public CouponController(CreateCoupon createCoupon, GetCoupon getCoupon, DeleteCoupon deleteCoupon) {
        this.createCoupon = createCoupon;
        this.getCoupon = getCoupon;
        this.deleteCoupon = deleteCoupon;
    }

    @PostMapping
    @Operation(summary = "Create a coupon")
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CreateCouponRequest request) {
        Coupon coupon = createCoupon.execute(
            request.code(),
            request.description(),
            request.discountValue(),
            request.expirationDate(),
            request.published()
        );
        return ResponseEntity.status(201).body(CouponResponse.from(coupon));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a coupon by ID")
    public ResponseEntity<CouponResponse> getById(@PathVariable UUID id) {
        Coupon coupon = getCoupon.execute(id);
        return ResponseEntity.ok(CouponResponse.from(coupon));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Soft delete a coupon")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteCoupon.execute(id);
        return ResponseEntity.noContent().build();
    }
}

package com.challenge.coupon.infrastructure.config;

import com.challenge.coupon.application.CreateCoupon;
import com.challenge.coupon.application.DeleteCoupon;
import com.challenge.coupon.application.GetCoupon;
import com.challenge.coupon.domain.CouponRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public CreateCoupon createCoupon(CouponRepositoryPort repository) {
        return new CreateCoupon(repository);
    }

    @Bean
    public GetCoupon getCoupon(CouponRepositoryPort repository) {
        return new GetCoupon(repository);
    }

    @Bean
    public DeleteCoupon deleteCoupon(CouponRepositoryPort repository) {
        return new DeleteCoupon(repository);
    }
}

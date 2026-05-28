package com.challenge.coupon.infrastructure.persistence;

import com.challenge.coupon.domain.Coupon;
import com.challenge.coupon.domain.CouponCode;
import com.challenge.coupon.domain.CouponRepositoryPort;
import com.challenge.coupon.domain.CouponStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class CouponPersistenceAdapter implements CouponRepositoryPort {

    private final CouponJpaRepository jpaRepository;

    public CouponPersistenceAdapter(CouponJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity entity = toEntity(coupon);
        jpaRepository.save(entity);
        return coupon;
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    private CouponEntity toEntity(Coupon coupon) {
        CouponEntity entity = new CouponEntity();
        entity.setId(coupon.getId());
        entity.setCode(coupon.getCode().getValue());
        entity.setDescription(coupon.getDescription());
        entity.setDiscountValue(coupon.getDiscountValue());
        entity.setExpirationDate(coupon.getExpirationDate());
        entity.setStatus(coupon.getStatus().name());
        entity.setPublished(coupon.isPublished());
        entity.setRedeemed(coupon.isRedeemed());
        return entity;
    }

    private Coupon toDomain(CouponEntity entity) {
        return new Coupon(
            entity.getId(),
            CouponCode.reconstitute(entity.getCode()),
            entity.getDescription(),
            entity.getDiscountValue(),
            entity.getExpirationDate(),
            CouponStatus.valueOf(entity.getStatus()),
            entity.isPublished(),
            entity.isRedeemed()
        );
    }
}

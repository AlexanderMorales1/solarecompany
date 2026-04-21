package com.solare.repository;

import com.solare.model.entity.DiscountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRepository extends JpaRepository<DiscountEntity, Long> {
    List<DiscountEntity> findByActiveTrue();
}

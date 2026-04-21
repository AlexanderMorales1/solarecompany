package com.solare.repository;

import com.solare.model.entity.BrandEntity;
import com.solare.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {

    List<ProductEntity> findByBrandAndFeaturedTrue(BrandEntity brand);

    List<ProductEntity> findByFeaturedTrue();
}

package com.solare.repository;

import com.solare.model.entity.CartEntity;
import com.solare.model.entity.ProductEntity;
import com.solare.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findByUser(UserEntity user);

    Optional<CartEntity> findByUserAndProduct(UserEntity user, ProductEntity product);

    void deleteByUser(UserEntity user);
}

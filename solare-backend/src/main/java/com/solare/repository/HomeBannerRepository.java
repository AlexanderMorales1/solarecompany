package com.solare.repository;

import com.solare.model.entity.HomeBannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeBannerRepository extends JpaRepository<HomeBannerEntity, Long> {
    List<HomeBannerEntity> findByActiveTrueOrderByDisplayOrderAscCreatedAtAsc();
}

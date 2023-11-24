package com.tomato.market.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.LocationEntity;

public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {

	LocationEntity findByUserId(String userId);
}

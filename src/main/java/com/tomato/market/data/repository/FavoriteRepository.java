package com.tomato.market.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.FavoriteEntity;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Integer> {

}

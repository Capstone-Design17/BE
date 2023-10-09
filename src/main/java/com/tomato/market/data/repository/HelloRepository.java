package com.tomato.market.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.HelloEntity;

public interface HelloRepository extends JpaRepository<HelloEntity, String> {

}

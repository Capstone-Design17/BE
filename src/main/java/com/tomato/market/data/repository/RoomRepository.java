package com.tomato.market.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tomato.market.data.entity.RoomEntity;

public interface RoomRepository extends JpaRepository<RoomEntity, Integer> {
	RoomEntity findByUserIdAndPostNum(String userId, Integer postNum);

	List<RoomEntity> findBySellerIdOrUserId(String sellerId, String userId);
}

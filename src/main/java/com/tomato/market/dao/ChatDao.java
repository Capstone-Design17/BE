package com.tomato.market.dao;

import com.tomato.market.data.entity.RoomEntity;

public interface ChatDao {
	RoomEntity findRoomNum(String userId, Integer postNum);

	RoomEntity save(RoomEntity roomEntity);
}
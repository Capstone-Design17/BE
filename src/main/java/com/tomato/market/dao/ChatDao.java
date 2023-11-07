package com.tomato.market.dao;

import java.util.List;

import com.tomato.market.data.collection.ChatCollection;
import com.tomato.market.data.entity.RoomEntity;

public interface ChatDao {
	RoomEntity findRoomId(String userId, Integer postNum);

	RoomEntity save(RoomEntity roomEntity);

	ChatCollection save(ChatCollection chatCollection);

	List<ChatCollection> findByRoomId(String roomId);

	List<RoomEntity> findBySellerIdOrUserId(String sellerId, String userId);
}

package com.tomato.market.data.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tomato.market.data.collection.ChatCollection;

public interface ChatRepository extends MongoRepository<ChatCollection, String> {
	List<ChatCollection> findByRoomId(String roomId);
}

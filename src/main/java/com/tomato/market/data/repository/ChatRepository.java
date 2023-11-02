package com.tomato.market.data.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tomato.market.data.document.ChatDocument;

public interface ChatRepository extends MongoRepository<ChatDocument, String> {
}

package com.tomato.market.data.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class PostEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer postNum;
	Integer userNum; // 관계를 가지는 키 값을 어떻게 구축?
	String location;
	String title;
	String category;
	String content;
	Integer price;
	String detailLocation;
	Integer status;
	Date createdAt; // 애노테이션?
	String boughtUser;
}

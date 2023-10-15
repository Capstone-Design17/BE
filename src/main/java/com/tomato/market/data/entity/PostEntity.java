package com.tomato.market.data.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EntityListeners(AuditingEntityListener.class)
@Table(name = "post")
public class PostEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer postNum;
	String userId; // 관계를 가지는 키 값을 어떻게 구축?
	String location;
	String title;
	String category;
	String content;
	Integer price;
	String detailLocation;
	Integer status;
	@CreatedDate
	Date createdAt; // 애노테이션?
	String boughtUserId;
}

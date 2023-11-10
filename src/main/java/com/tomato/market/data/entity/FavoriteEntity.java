package com.tomato.market.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "favorite")
public class FavoriteEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer favoriteNum;
	private String userId;
	private Integer postNum;
	private Integer status; // 1 등록 0 취소
}

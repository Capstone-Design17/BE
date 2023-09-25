package com.tomato.market.data.entity;

import java.util.Date;

import jakarta.persistence.Entity;
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
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer userNum;
	String id;
	String pwd;
	String realName;
	String nickName;
	String phone;
	String email;
	Date birthday;
	String lastLogin;
	Integer status;
	String resignReason;

}

package com.tomato.market.dao;

import com.tomato.market.data.entity.UserEntity;

public interface UserDao {
	public UserEntity save(UserEntity userEntity);

	public UserEntity get(UserEntity userEntity); // String id?

	public boolean existsByEmail(String userEmail);

	/*  ### 미구현 ###
		아이디 중복 체크
		핸드폰 중복 체크
	 	넥네임 중복 체크
	*/

}

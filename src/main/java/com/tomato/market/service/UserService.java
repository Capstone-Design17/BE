package com.tomato.market.service;

import com.tomato.market.data.dto.UserDto;

public interface UserService {
	public UserDto registerUser(UserDto userDto);

	public UserDto loginUser(UserDto userDto);
}

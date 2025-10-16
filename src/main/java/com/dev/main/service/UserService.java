package com.dev.main.service;

import java.util.List;

import com.dev.main.dto.UserDto;
import com.dev.main.model.User;

public interface UserService {
	List<User>getAllUsers();
	List<User> getAllUsersByName(String name);
	User getUserByEmail(String email);
	
	void createUser(UserDto userDto);
	
	void initializeDefaultAdminUser(String name);
}

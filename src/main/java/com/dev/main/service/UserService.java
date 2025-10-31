package com.dev.main.service;

import java.util.List;

import com.dev.main.dto.UserDto;
import com.dev.main.model.User;

public interface UserService {
	List<User>getAllUsers();
	List<User> getAllUsersByName(String name);
	User getUserByEmail(String email);
	User getUserById(Long id);
	void createUser(UserDto userDto,boolean isAdminRole);
	void initializeDefaultAdminUser(String name);
	void disableUser(Long id);
	void enableUser(Long id);
}

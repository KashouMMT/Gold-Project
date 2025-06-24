package com.dev.main.service;

import java.util.List;

import com.dev.main.model.Role;

public interface RoleService {
	List<Role>getAllRoles();
	void initializeRoles(List<String> roles) throws Exception;
}

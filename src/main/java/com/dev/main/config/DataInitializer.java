package com.dev.main.config;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.dev.main.service.RoleService;
import com.dev.main.service.UserService;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {
	
	private final RoleService roleService;
	private final UserService userService;
	
	public DataInitializer(RoleService roleService, UserService userService) {
		super();
		this.roleService = roleService;
		this.userService = userService;
	}

	@PostConstruct
	public void init() throws Exception {
		roleService.initializeRoles(Arrays.asList("ROLE_ADMIN","ROLE_USER"));
		userService.initializeDefaultAdminUser("admin");
	}
}

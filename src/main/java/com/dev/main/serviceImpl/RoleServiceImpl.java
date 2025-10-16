package com.dev.main.serviceImpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.dev.main.model.Role;
import com.dev.main.repository.RoleRepository;
import com.dev.main.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	
	private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
	
	private final RoleRepository roleRepo;
	
	public RoleServiceImpl(RoleRepository roleRepo) {
		this.roleRepo = roleRepo;
	}
	
	@Override
	public List<Role> getAllRoles() {
		logger.info("Fetching all roles from the database.");
		List<Role> roles = roleRepo.findAll();
		logger.debug("Total roles retrieved: {}", roles.size());
		return roles;
	}

	@Override
	public void initializeRoles(List<String> roles) throws Exception {
		logger.info("Initializing default roles: {}", roles);
		for(String roleName : roles) {
			try {
				if(roleRepo.findByRoleName(roleName).isEmpty()) {
					logger.debug("Role [{}] not found. Creating new role.", roleName);
					Role role = new Role();
					role.setRoleName(roleName);
					roleRepo.save(role);
					logger.info("Role [{}] created successfully.", roleName);
				} else {
					logger.debug("Role [{}] already exists. Skipping creation.", roleName);
				}
			} catch(DataAccessException e) {
				logger.error("Database access error while initializing role [{}]: {}", roleName, e.getMessage());
				throw new Exception("Error while accessing the database for role: " + roleName, e);
			} catch (Exception e ) {
				logger.error("Unexpected error while initializing role [{}]: {}", roleName, e.getMessage());
				throw new Exception("Error while accessing the database for role: " + roleName, e);
			}
		}
	}
}

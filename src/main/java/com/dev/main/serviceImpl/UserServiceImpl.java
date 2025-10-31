package com.dev.main.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dev.main.dto.UserDto;
import com.dev.main.model.Role;
import com.dev.main.model.User;
import com.dev.main.repository.RoleRepository;
import com.dev.main.repository.UserRepository;
import com.dev.main.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private final UserRepository userRepo;
	private final RoleRepository roleRepo;
	private final PasswordEncoder encoder;

	public UserServiceImpl(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
		super();
		this.userRepo = userRepo;
		this.roleRepo = roleRepo;
		this.encoder = encoder;
	}

	@Override
	public List<User> getAllUsers() {
		logger.info("Fetching all users from the database.");
		List<User> users = userRepo.findAll();
		logger.debug("Number of users retrieved: {}", users.size());
		return users;
	}

	@Override
	public List<User> getAllUsersByName(String name) {
		logger.info("Fetching users with name: {}", name);
		List<User> users = userRepo.findByName(name);
		logger.debug("Users found with name [{}]: {}", name, users.size());
		return users;
	}

	@Override
	public User getUserByEmail(String email) {
		logger.info("Fetching user by email: {}", email);
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            logger.debug("User [{}] found in the database.", email);
        } else {
            logger.warn("User [{}] not found in the database.", email);
        }
        return user.orElse(null);
	}

	@Override
	public void initializeDefaultAdminUser(String email) {
		logger.info("Checking if default admin user needs to be initialized.");
		
		User existing = getUserByEmail(email);
		
		if (existing == null) {
			logger.debug("No users found. Creating default admin user.");
			Set<Role> role = new HashSet<>();
			role.add(roleRepo.findByRoleName("ROLE_ADMIN").orElseThrow(() -> new IllegalStateException()));
			User user = new User();
			user.setEmail(email);
			user.setName("admin");
			user.setEnabled(true);
			user.setPassword(encoder.encode("admin111"));
			user.setRoles(role);
			userRepo.save(user);
			logger.info("Default admin user initialized with username: admin");
		} else {
			logger.debug("Users already exist. Skipping default admin initialization.");
		}
	}

	@Override
	public void createUser(UserDto userDto,boolean isAdminRole) {
		logger.info("Creating new user with email: {}", userDto.getEmail());
		User user = new User();
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setPassword(encoder.encode(userDto.getPassword()));
		user.setEnabled(true);
		Set<Role> role = new HashSet<>();
		if(isAdminRole) role.add(roleRepo.findByRoleName("ROLE_ADMIN").orElse(null));
		else role.add(roleRepo.findByRoleName("ROLE_USER").orElse(null));
		user.setRoles(role);
		userRepo.save(user);
		logger.info("User [{}] created successfully.", userDto.getEmail());
        logger.debug("User details: name={}, enabled={}, roles={}", userDto.getName(), true, role);
	}

	@Override
	public void disableUser(Long id) {
		User user = getUserById(id);
		user.setEnabled(false);
		userRepo.save(user);
	}

	@Override
	public User getUserById(Long id) {
		return userRepo.findById(id).orElse(null);
	}

	@Override
	public void enableUser(Long id) {
		User user = getUserById(id);
		user.setEnabled(true);
		userRepo.save(user);
	}
}

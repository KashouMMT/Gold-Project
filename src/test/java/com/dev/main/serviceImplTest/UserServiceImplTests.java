package com.dev.main.serviceImplTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dev.main.model.Role;
import com.dev.main.model.User;
import com.dev.main.repository.RoleRepository;
import com.dev.main.repository.UserRepository;
import com.dev.main.serviceImpl.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
	
	@Mock UserRepository userRepo;
	@Mock RoleRepository roleRepo;
	@Mock PasswordEncoder encoder;
	
	@InjectMocks UserServiceImpl service;
	
	@Test
	void initializeDefaultAdmin_WhenNoUsersExistInDB_WithCorrect_Email_EncodedPassword_Role() {
		// repo empty
		when(userRepo.findAll()).thenReturn(List.of());
		Role adminRole = new Role();
		adminRole.setRoleName("ROLE_ADMIN");
		when(roleRepo.findByRoleName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
		when(encoder.encode("admin111")).thenReturn("$bcrypt$encoded");
		
		service.initializeDefaultAdminUser("admin@admin.com");
		
		// serve user inspected via captor 
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepo).save(userCaptor.capture());
		User saved = userCaptor.getValue();
		
		assertThat(saved.getName()).isEqualTo("admin");
		assertThat(saved.getEmail()).isEqualTo("admin@admin.com");
		assertThat(saved.isEnabled()).isTrue();
		assertThat(saved.getPassword()).isEqualTo("$bcrypt$encoded");
		assertThat(saved.getRoles()).extracting(Role::getRoleName).contains("ROLE_ADMIN");
		
		verify(encoder).encode("admin111");
		verify(roleRepo).findByRoleName("ROLE_ADMIN");
		verify(userRepo).findAll();
		verifyNoMoreInteractions(userRepo, roleRepo, encoder);
	}
	
	@Test
	void initializeDefaultAdmin_WhenUsersExistInDB_WithCorrect_Email_EncodedPassword_Role() {
		when(userRepo.findAll()).thenReturn(List.of(new User()));
		Role adminRole = new Role();
		adminRole.setRoleName("ROLE_ADMIN");
		when(roleRepo.findByRoleName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
		when(encoder.encode("admin111")).thenReturn("$bcrypt$encoded");
		
		service.initializeDefaultAdminUser("admin@admin.com");
		
		ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
		verify(userRepo).save(userCaptor.capture());
		User saved = userCaptor.getValue();
		
		assertThat(saved.getName()).isEqualTo("admin");
		assertThat(saved.getEmail()).isEqualTo("admin@admin.com");
		assertThat(saved.isEnabled()).isTrue();
		assertThat(saved.getPassword()).isEqualTo("$bcrypt$encoded");
		assertThat(saved.getRoles()).extracting(Role::getRoleName).contains("ROLE_ADMIN");
		
		verify(encoder).encode("admin111");
		verify(roleRepo).findByRoleName("ROLE_ADMIN");
		verify(userRepo).findAll();
		verifyNoMoreInteractions(userRepo, roleRepo, encoder);
	}
}

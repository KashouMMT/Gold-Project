package com.dev.main.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dev.main.model.Role;
import com.dev.main.model.User;


public class MyUserDetails implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(MyUserDetails.class);

	private final User user;
	
	public MyUserDetails(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Set<Role> roles = user.getRoles();
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		for(Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
		}
		logger.debug("Granted authorities for user [{}]: {}", user.getEmail(), authorities);
		return authorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}
	
	public User getUser() {
		return this.user;
	}
	
	public boolean isAdmin() {
		return user.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ROLE_ADMIN"));
	}
	
	public boolean isUser() {
		return user.getRoles().stream().anyMatch(role -> role.getRoleName().equals("ROLE_USER"));
	}
}

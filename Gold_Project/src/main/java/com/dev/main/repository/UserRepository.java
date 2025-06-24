package com.dev.main.repository;

import org.springframework.stereotype.Repository;

import com.dev.main.model.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
	Optional<User> findByEmail(String email);
	List<User> findByName(String name);
}

package com.dev.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.main.model.AppSecret;

@Repository
public interface AppSecretRepository extends JpaRepository<AppSecret,String>{

}

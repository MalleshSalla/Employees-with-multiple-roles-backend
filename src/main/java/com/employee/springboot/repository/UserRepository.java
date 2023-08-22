package com.employee.springboot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.springboot.entity.User;



public interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findByEmail(String email);
	
	Optional<User> findByUserNameOrEmail(String userName, String email);
	
	Optional<User> findByUserName(String userName);
	
	Boolean existsByUserName(String userName);
	
	Boolean existsByEmail(String email);

}
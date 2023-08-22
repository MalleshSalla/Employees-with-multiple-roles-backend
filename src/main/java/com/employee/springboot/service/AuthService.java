package com.employee.springboot.service;


import java.util.List;

import org.springframework.http.HttpStatusCode;

import com.employee.springboot.entity.Privilege;
import com.employee.springboot.entity.User;
import com.employee.springboot.payload.LoginDto;
import com.employee.springboot.payload.RegisterDto;
import com.employee.springboot.payload.RoleDto;
import com.employee.springboot.payload.UserDto;

public interface AuthService {
	
	UserDto login(LoginDto loginDto);

	RegisterDto  register(RegisterDto registerDto);
	
	RoleDto addRole(RoleDto roleDto);

	Privilege addPrivilege(String name);
	
	RoleDto updateRole(RoleDto roleDto,Long roleId);
	
	RegisterDto updateUser(RegisterDto user,Long Id) ;
		
	RegisterDto addUser(RegisterDto user);

	List<RegisterDto> getAllUsers();

	RegisterDto getUserById(Long id);

	String getRoleById(Long id);

	void deleteUserById(Long id);
	
}

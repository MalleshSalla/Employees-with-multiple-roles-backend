package com.employee.springboot.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.employee.springboot.entity.Privilege;
import com.employee.springboot.payload.LoginDto;
import com.employee.springboot.payload.RegisterDto;
import com.employee.springboot.payload.RoleDto;
import com.employee.springboot.payload.UserDto;
import com.employee.springboot.service.AuthService;

@RestController
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());


	@PostMapping("/login")
	public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto) {
		
		UserDto dto = authService.login(loginDto);
		log.info(loginDto.getUserNameOrEmail()+" is logged in " );
		return new ResponseEntity<UserDto>(dto, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<RegisterDto> register(@RequestBody RegisterDto registerDto) {
		
		RegisterDto response = authService.register(registerDto);
		log.info(registerDto.getName()+" is a new user added:");
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/addRole")
	public ResponseEntity<RoleDto> addRole(@RequestBody RoleDto roleDto) {
		RoleDto dto = authService.addRole(roleDto);
		log.info(roleDto.getName()+" is a new role added:");
		return new ResponseEntity<RoleDto>(dto, HttpStatus.CREATED);
	}
	
	@GetMapping("/getRoleById/{id}")
	public ResponseEntity<String> getRoleById(@PathVariable("id") Long id) {
		String response = authService.getRoleById(id);
		log.info( "Role id "+id+" is retrieved ");
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}
	
	@PutMapping("/updateRoleById/{id}")
	public ResponseEntity<RoleDto> updateRoleById(@RequestBody RoleDto roleDto,@PathVariable("id") Long id){
		RoleDto dto = authService.updateRole(roleDto, id);
		log.info("Role is updated successfully with id "+id);
		
		return new ResponseEntity<RoleDto>(dto,HttpStatus.ACCEPTED);
	}
	

	@PostMapping("/addPrivilege")
	public ResponseEntity<Privilege> addPrivilege(@RequestBody Privilege privilege) {
		
		Privilege privilege2 = authService.addPrivilege(privilege.getName());
		log.info("new privilage is added :"+ privilege.getName());
		return new ResponseEntity<Privilege>(privilege2, HttpStatus.CREATED);
	}

}

package com.employee.springboot.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.employee.springboot.payload.RegisterDto;
import com.employee.springboot.service.AuthService;

@RestController
public class UserController {

	@Autowired
	private AuthService authService;
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/addNewUser")
	public ResponseEntity<RegisterDto> addUser(@RequestBody RegisterDto registerDto) {
		
		RegisterDto dto = authService.addUser(registerDto);
		log.info("New User is Added with name: "+registerDto.getName() );
		return new ResponseEntity<RegisterDto>(dto, HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('READ_DATA')")
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<RegisterDto>> getAllUsers() {
		
		List<RegisterDto> dtos = authService.getAllUsers();
		log.info("Retrieved All users");
		return new ResponseEntity<List<RegisterDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping("/getUserById/{id}")
	public ResponseEntity<RegisterDto> getUserById(@PathVariable("id") Long id) {

		return new ResponseEntity<RegisterDto>(authService.getUserById(id), HttpStatus.OK);
	}

	@DeleteMapping("deleteUserById/{id}")
	public void deleteUserById(@PathVariable("id") Long id) {
		authService.deleteUserById(id);
	}

	@PreAuthorize("hasAuthority('UPDATE_USER')")
	@PutMapping("/updateUserById/{id}")
	public ResponseEntity<RegisterDto> updateUser(@RequestBody RegisterDto user, @PathVariable("id") Long id) {
		RegisterDto savedUser = authService.updateUser(user, id);
		return new ResponseEntity<RegisterDto>(savedUser, HttpStatus.ACCEPTED);
	}

}

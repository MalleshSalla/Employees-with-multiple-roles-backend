package com.employee.springboot.payload;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto 
{
	private long id;
	private String name;
	private String userName;
	private String email;
	private String password;
	private long userRole;
	private String roles;
	private List<String> authorities;
	
}
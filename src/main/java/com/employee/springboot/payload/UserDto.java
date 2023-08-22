package com.employee.springboot.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private long id;
	private String name;
	private String userName;
	private String email;
	private String password;
	private String roles;
	private List<String> authorities;
	private String accessToken;
	private String tokenType="Bearer";
}

package com.employee.springboot.payload;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RoleDto {
	
	private long id;
	private String name;
	private Set<Long> privilagesIds;
	
}

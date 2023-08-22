package com.employee.springboot.payload;

import java.util.HashSet;
import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {
	
	private long Id;
	
	private String name;
	
	private String address;
	
	private Set<DepartmentDto> department = new HashSet<>();
	
}

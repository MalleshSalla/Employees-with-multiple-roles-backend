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
public class DepartmentDto {
	
	private long id;
	private String departmentName;
	private String departmentDescription;
	private Set<EmployeeDto> employee=new HashSet<>();

}

package com.employee.springboot.service;

import java.util.List;

import com.employee.springboot.payload.DepartmentDto;
import com.employee.springboot.payload.EmployeeDto;

public interface DepartmentService {

	DepartmentDto saveDepartment(long companyId, DepartmentDto department);

	List<DepartmentDto> getAllDepartments();

	List<EmployeeDto> getEmployeeDetailsByDepartmentId(Long departmentId);

	void deletDepartmentById(Long id);

	List<EmployeeDto> getEmployeeByDepartmentName(String str);

}

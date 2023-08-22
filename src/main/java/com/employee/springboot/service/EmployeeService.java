package com.employee.springboot.service;

import java.util.List;
import com.employee.springboot.payload.EmployeeDto;

public interface EmployeeService {

	EmployeeDto saveEmployee(Long departmentId, Long companyId, EmployeeDto employeeDto);

	List<EmployeeDto> getAllEmployees();

	EmployeeDto getEmployeeById(Long employeeId);

	EmployeeDto updateEmployeeById(EmployeeDto employeeDto, Long employeeId);

	void deleteEmployeeById(Long employeeId);

	List<EmployeeDto> getEmployeeByCompanyId(Long companyId);
	
	List<EmployeeDto> getSalaryBySort();

}

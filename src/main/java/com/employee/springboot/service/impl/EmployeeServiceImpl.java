package com.employee.springboot.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.springboot.entity.Company;
import com.employee.springboot.entity.Department;
import com.employee.springboot.entity.Employee;
import com.employee.springboot.exception.ResourceNotFoundException;
import com.employee.springboot.payload.EmployeeDto;
import com.employee.springboot.repository.CompanyRepository;
import com.employee.springboot.repository.DepartmentRepository;
import com.employee.springboot.repository.EmployeeRepository;
import com.employee.springboot.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public EmployeeDto saveEmployee(Long departmentId, Long companyId, EmployeeDto employeeDto) {

		Employee employee = mapToEntity(employeeDto);
		Department department = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department is not found with id" + departmentId,
						"departmentId", departmentId));
		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company is not found with id" + companyId,
						"companyId", companyId));
		employee.setDepartment(department);
		employee.setCompany(company);

		Employee newEmployee = employeeRepository.save(employee);
		EmployeeDto employeeDto1 = mapToDto(newEmployee);
		return employeeDto1;
	}

	@Override
	public List<EmployeeDto> getAllEmployees() {

		List<Employee> listOfEmployees = employeeRepository.findAll();

		List<EmployeeDto> listOfEmployeesDto = listOfEmployees.stream().map(employee -> mapToDto(employee))
				.collect(Collectors.toList());
		return listOfEmployeesDto;
	}

	@Override
	public EmployeeDto getEmployeeById(Long employeeId) {

		Employee employee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee is not found with id!" + employeeId,
						"employeeId", employeeId));
		return mapToDto(employee);
	}

	@Override
	public EmployeeDto updateEmployeeById(EmployeeDto employeeDto, Long employeeId) {

		Employee dbEmployee = employeeRepository.findById(employeeId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee is not found with id!" + employeeId,
						"employeeId", employeeId));

		dbEmployee.setName(employeeDto.getName());
		dbEmployee.setSalary(employeeDto.getSalary());
		dbEmployee.setEmail(employeeDto.getEmail());

		Employee updatedEmployee = employeeRepository.save(dbEmployee);
		EmployeeDto employeeDto2 = mapToDto(updatedEmployee);
		return employeeDto2;
	}

	@Override
	public void deleteEmployeeById(Long employeeId) {

		Employee employee = employeeRepository.findById(employeeId).orElseThrow(
				() -> new ResourceNotFoundException("Employee not exist with id:" + employeeId, null, employeeId));
		employeeRepository.delete(employee);

	}

	private EmployeeDto mapToDto(Employee employee) {
		EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
		return employeeDto;
	}

	private Employee mapToEntity(EmployeeDto employeeDto) {
		Employee employee = mapper.map(employeeDto, Employee.class);
		return employee;
	}

	@Override
	public List<EmployeeDto> getEmployeeByCompanyId(Long companyId) {
		
		List<Employee> employees = employeeRepository.findAll();
		List<Employee> listOfEmplyees = employees.stream().filter(e -> e.getCompany().getId() == companyId)
				.collect(Collectors.toList());
		List<EmployeeDto> employeesDto = listOfEmplyees.stream().map(e -> mapToDto(e)).collect(Collectors.toList());
		return employeesDto;
	}

	@Override
	public List<EmployeeDto> getSalaryBySort() {
		List<Employee> employee = employeeRepository.findAll();
		List<Employee> getSal = employee.stream().filter(s -> s.getSalary() > 80000.0).collect(Collectors.toList());
		List<EmployeeDto> employeeDto = getSal.stream().map(s -> mapToDto(s)).collect(Collectors.toList());
		return employeeDto;
	}

}

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
import com.employee.springboot.payload.DepartmentDto;
import com.employee.springboot.payload.EmployeeDto;
import com.employee.springboot.repository.CompanyRepository;
import com.employee.springboot.repository.DepartmentRepository;
import com.employee.springboot.repository.EmployeeRepository;
import com.employee.springboot.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Override
	public DepartmentDto saveDepartment(long companyId, DepartmentDto departmentDto) {

		Department department = mapToEntity(departmentDto);
		Company comapany = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Department is not found with id " + companyId,
						"companyId", companyId));
		department.setCompany(comapany);
		
		Department savedDepartment = departmentRepository.save(department);
		return mapToDto(savedDepartment);
	}

	@Override
	public List<DepartmentDto> getAllDepartments() {

		List<Department> listOfDepartments = departmentRepository.findAll();

		List<DepartmentDto> listOfDepartmentsDto = listOfDepartments.stream().map(department -> mapToDto(department))
				.collect(Collectors.toList());
		return listOfDepartmentsDto;
	}

	@Override
	public List<EmployeeDto> getEmployeeDetailsByDepartmentId(Long departmentId) {

		Department departments = departmentRepository.findById(departmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Department is not found with id!" + departmentId,
						"comapanyId", departmentId));

		List<Employee> employee = departments.getEmployee().stream()
				.filter(e -> e.getDepartment().getId() == departmentId).collect(Collectors.toList());

		List<EmployeeDto> employeeDtos = employee.stream().map(e -> mapToDto(e)).collect(Collectors.toList());
		return employeeDtos;
	}

	@Override
	public void deletDepartmentById(Long id) {
		departmentRepository.deleteById(id);
	}

	private DepartmentDto mapToDto(Department department) {

		DepartmentDto departmentDto = mapper.map(department, DepartmentDto.class);
		return departmentDto;
	}

	private Department mapToEntity(DepartmentDto departmentDto) {
		
		Department department = mapper.map(departmentDto, Department.class);
		return department;
	}

	private EmployeeDto mapToDto(Employee employee) {
		
		EmployeeDto employeeDto = mapper.map(employee, EmployeeDto.class);
		return employeeDto;
	}

	public List<EmployeeDto> getEmployeeByDepartmentName(String departmentName) {

		List<Employee> employees = employeeRepository.findAll();

		List<EmployeeDto> listOfEmployeesByDept = employees.stream()
				.filter(s -> s.getDepartment().getDepartmentName().equals(departmentName)).map(s -> mapToDto(s))
				.collect(Collectors.toList());
		return listOfEmployeesByDept;
	}

}

package com.employee.springboot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.employee.springboot.payload.DepartmentDto;
import com.employee.springboot.payload.EmployeeDto;
import com.employee.springboot.service.DepartmentService;

@RestController
@CrossOrigin(origins = "http://localhost:4200/**")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/saveDepartments/{companyId}")
	public ResponseEntity<DepartmentDto> saveDepartment(@PathVariable("companyId") long companyId,
			@RequestBody DepartmentDto departmentDto) {

		return new ResponseEntity<DepartmentDto>(departmentService.saveDepartment(companyId, departmentDto),
				HttpStatus.CREATED);
	}

	@PreAuthorize("hasAuthority('READ_DATA')")
	@GetMapping("/getAllDepartments")
	public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
		return new ResponseEntity<List<DepartmentDto>>(departmentService.getAllDepartments(), HttpStatus.OK);
	}

	@PreAuthorize("hasAuthority('READ_DATA')")
	@GetMapping("/getEmployeesByDepartment/{departmentId}")
	public List<EmployeeDto> getEmployeeByDepartmentId(@PathVariable("departmentId") Long departmentId) {
		return departmentService.getEmployeeDetailsByDepartmentId(departmentId);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/deleteDepartment/{id}")
	public ResponseEntity<String> deleteDepartmentById(@PathVariable("id") Long id) {

		departmentService.deletDepartmentById(id);
		return new ResponseEntity<String>("Department deleted successfully! ", HttpStatus.OK);
	}

	@GetMapping("/getEmpByDeptName/{str}")
	public ResponseEntity<List<EmployeeDto>> getEmployeesByDepartmentname(@PathVariable("str") String str) {
		return new ResponseEntity<List<EmployeeDto>>(departmentService.getEmployeeByDepartmentName(str), HttpStatus.OK);
	}

}

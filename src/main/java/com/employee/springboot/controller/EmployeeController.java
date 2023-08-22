package com.employee.springboot.controller;


import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.employee.springboot.payload.EmployeeDto;
import com.employee.springboot.service.EmployeeService;

@RestController
//@CrossOrigin(origins = "http://localhost:4200/**")
public class EmployeeController {
	
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private EmployeeService employeeService;
	
	//@PreAuthorize("hasRole('ADMIN')")
	@PreAuthorize("hasAuthority('ADD_EMPLOYEE')")
	@PostMapping("/saveEmployee/{departmentId}/{companyId}")
	public ResponseEntity<EmployeeDto> saveEmployee(@PathVariable("departmentId") Long departmentId
			                                    ,@PathVariable("companyId") Long companyId
			                                     ,@RequestBody EmployeeDto employeeDto) {
		
		EmployeeDto dto = employeeService.saveEmployee(departmentId,companyId,employeeDto);
		log.info("Employee is added with name: " +employeeDto.getName());
		return new ResponseEntity<>(dto,HttpStatus.CREATED);
	}
	
	
	@PreAuthorize("hasAuthority('GET_EMPLOYEE')")
	@GetMapping("/getAllEmployees")
	public ResponseEntity<List<EmployeeDto>> getAllEmployees(){
		List<EmployeeDto> dtos = employeeService.getAllEmployees();
		log.info("Retrieved all the employees");
		return new ResponseEntity<>(dtos,HttpStatus.OK);
	}

	
	@PreAuthorize("hasAuthority('GET_EMPLOYEE')")
	@GetMapping("/getEmployeeById/{id}")
	public ResponseEntity<EmployeeDto>  getEmployeeById(@PathVariable("id") Long employeeId){	
		EmployeeDto dto = employeeService.getEmployeeById(employeeId);
		log.info(" employee retrieved by id : " + employeeId);
		return new ResponseEntity<EmployeeDto>(dto ,HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAuthority('UPDATE_EMPLOYEE')")
	@PutMapping("/updateEmployeeById/{id}")
	public ResponseEntity<EmployeeDto>  updateEmployeeById(@RequestBody EmployeeDto employeeDto
			                                                ,@PathVariable("id") Long employeeId) {
		
		EmployeeDto dto = employeeService.updateEmployeeById(employeeDto,employeeId);
		
		log.info("Employee updated with id: "+ employeeId);
		
		return new ResponseEntity<EmployeeDto>(dto,HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasAuthority('DELETE_EMPLOYEE')")
	@DeleteMapping("/deleteEmployeeById/{id}")
	public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") Long employeeId) {
		
		employeeService.deleteEmployeeById(employeeId);
		log.info("One Employee deleted with id: "+employeeId );
		return new ResponseEntity<String>("Employee deleted successfully with id: "+employeeId,HttpStatus.OK);		
	}
	
	
	@PreAuthorize("hasAuthority('GET_EMPLOYEE')")
	@GetMapping("/getEmployeesByCompanyId/{companyId}")
	public ResponseEntity<List<EmployeeDto>> getEmployeesByCompanyId(@PathVariable("companyId") Long companyId){
		
		List<EmployeeDto> dtos = employeeService.getEmployeeByCompanyId(companyId);
		
		log.info("All Employee are retrieved");
		return new ResponseEntity<List<EmployeeDto>>(dtos,HttpStatus.OK);
	}
	
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("/getSal")
	public ResponseEntity<List<EmployeeDto>> getSalBySort(){
		List<EmployeeDto> employee = employeeService.getSalaryBySort();
		return new ResponseEntity<List<EmployeeDto>>(employee,HttpStatus.OK);
	}

}

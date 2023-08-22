package com.employee.springboot.controller;

import java.util.List;

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

import com.employee.springboot.payload.CompanyDto;
import com.employee.springboot.service.CompanyService;

@RestController
public class CompanyController {
	
	@Autowired
	private CompanyService companyService;
	
	//@PreAuthorize("hasRole('ADMIN')")

	@PostMapping("/saveCompany")
	public ResponseEntity<CompanyDto>  saveCompany(@RequestBody CompanyDto companyDto) {
		
		return new ResponseEntity<CompanyDto> (companyService.saveCompanies(companyDto),HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAuthority('READ_DATA')")
	@GetMapping("/getAllCompanies")
	public ResponseEntity<List<CompanyDto>> getAllComapany(){
		
		return new ResponseEntity<List<CompanyDto>>(companyService.getAllDepartments(),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
	@PutMapping("/updateCompany")
	public ResponseEntity<CompanyDto> updateComapany(@RequestBody CompanyDto companyDto,Long companyId){
		
		CompanyDto savedcompanyDto = companyService.updateCompany(companyDto,companyId);
		
		return new ResponseEntity<CompanyDto>(savedcompanyDto,HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN') OR hasRole('USER')")
	@DeleteMapping("/deleteCompany/{companyId}")
	public ResponseEntity<String>  deleteCompany(@PathVariable("companyId") long companyId){
		companyService.deleteCompanyById(companyId);
		return new ResponseEntity<String>("deleted department by id "+companyId,HttpStatus.OK);
	}

}

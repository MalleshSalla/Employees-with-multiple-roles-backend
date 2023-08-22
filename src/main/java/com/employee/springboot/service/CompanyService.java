package com.employee.springboot.service;

import java.util.List;

import com.employee.springboot.payload.CompanyDto;

public interface CompanyService {

	CompanyDto saveCompanies(CompanyDto companyDto);

	List<CompanyDto> getAllDepartments();

	void deleteCompanyById(long companyId);

	CompanyDto updateCompany(CompanyDto companyDto, Long companyId);

}

package com.employee.springboot.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.springboot.entity.Company;
import com.employee.springboot.exception.ResourceNotFoundException;
import com.employee.springboot.payload.CompanyDto;
import com.employee.springboot.repository.CompanyRepository;
import com.employee.springboot.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ModelMapper mapper;

	@Override
	public CompanyDto saveCompanies(CompanyDto companyDto) {

		Company company = mapToEntity(companyDto);
		Company savedCompany = companyRepository.save(company);

		return mapToDto(savedCompany);
	}

	@Override
	public List<CompanyDto> getAllDepartments() {

		List<Company> companies = companyRepository.findAll();
		List<CompanyDto> companyDto = companies.stream().map(e -> mapToDto(e)).collect(Collectors.toList());
		
		return companyDto;
	}

	@Override
	public void deleteCompanyById(long companyId) {

		// companyRepository.deleteById(companyId);
		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Company is", "companyId", companyId));
		companyRepository.delete(company);
	}

	@Override
	public CompanyDto updateCompany(CompanyDto companyDto, Long companyId) {

		Company company = companyRepository.findById(companyId)
				.orElseThrow(() -> new ResourceNotFoundException("Employee is" + companyId, "companyId", companyId));
		company.setName(companyDto.getName());
		company.setAddress(companyDto.getAddress());

		companyRepository.save(company);
		
		return mapToDto(company);
		
	}

	private Company mapToEntity(CompanyDto companyDto) {
		
		Company company = mapper.map(companyDto, Company.class);
		return company;
	}

	private CompanyDto mapToDto(Company company) {
		
		CompanyDto companyDto = mapper.map(company, CompanyDto.class);
		return companyDto;
	}

}

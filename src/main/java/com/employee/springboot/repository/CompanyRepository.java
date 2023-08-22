package com.employee.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.springboot.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Long>{
	
}

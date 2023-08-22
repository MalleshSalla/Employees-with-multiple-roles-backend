package com.employee.springboot.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	private double salary;
	
	private String email;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="department_id",nullable = false)
	private Department department;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="company_id",nullable = false)
	private Company company;
	
}

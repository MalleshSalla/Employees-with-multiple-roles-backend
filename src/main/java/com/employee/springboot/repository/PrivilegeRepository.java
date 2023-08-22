package com.employee.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.springboot.entity.Privilege;


public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);
}

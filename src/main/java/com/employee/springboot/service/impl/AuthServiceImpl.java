package com.employee.springboot.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.employee.springboot.entity.Privilege;
import com.employee.springboot.entity.Role;
import com.employee.springboot.entity.User;
import com.employee.springboot.exception.EmployeeApiException;
import com.employee.springboot.exception.ResourceNotFoundException;
import com.employee.springboot.payload.LoginDto;
import com.employee.springboot.payload.RegisterDto;
import com.employee.springboot.payload.RoleDto;
import com.employee.springboot.payload.UserDto;
import com.employee.springboot.repository.PrivilegeRepository;
import com.employee.springboot.repository.RoleRepository;
import com.employee.springboot.repository.UserRepository;
import com.employee.springboot.security.JwtTokenProvider;
import com.employee.springboot.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	private Authentication authentication;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	

	public UserDto login(LoginDto loginDto) {

		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUserNameOrEmail(), loginDto.getPassword()));
		} catch (Exception e) {
			throw new UsernameNotFoundException("User not found with user name " + loginDto.getUserNameOrEmail());

		}

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtTokenProvider.generateToken(authentication);

		User user = userRepository.findByUserName(loginDto.getUserNameOrEmail()).get();

		UserDto userDto = new UserDto();

		userDto.setId(user.getId());
		userDto.setName(user.getName());
		userDto.setUserName(user.getUserName());
		userDto.setEmail(user.getEmail());
		userDto.setPassword(user.getPassword());
		userDto.setRoles(user.getRole());
		userDto.setAuthorities(user.getAuthorities());
		userDto.setAccessToken(token);

		return userDto;
	}

	public RegisterDto register(RegisterDto registerDto) {

		// Check weather user exist with same user name and email
		if (userRepository.existsByUserName(registerDto.getUserName())) {
			throw new EmployeeApiException(HttpStatus.BAD_REQUEST, "Username is already exists");
		}

		// Check weather user exist with same email
		if (userRepository.existsByEmail(registerDto.getEmail())) {
			throw new EmployeeApiException(HttpStatus.BAD_REQUEST, "Email is already exists");
		}

		User user = new User();
		user.setName(registerDto.getName());
		user.setUserName(registerDto.getUserName());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		// set the role By default new Registered user is assigned with role "ROLE_USER"
		Role userRole = roleRepository.findByName("ROLE_USER").get();

		// list of all privileges objects under selected role.
		Collection<Privilege> userPrivileges = userRole.getPrivileges();

		Set<Role> roles = new HashSet<>();

		roles.add(userRole);
		user.setRoles(roles);
		user.setUserId(userRole.getId());
		user.setRole(userRole.getName());

		List<String> privilages = userPrivileges.stream().map(s -> s.getName()).collect(Collectors.toList());

		user.setAuthorities(privilages);

		User dbUSer = userRepository.save(user);

		registerDto.setId(dbUSer.getId());
		registerDto.setName(dbUSer.getName());
		registerDto.setUserName(dbUSer.getUserName());
		registerDto.setEmail(dbUSer.getEmail());
		registerDto.setPassword(dbUSer.getPassword());
		registerDto.setRoles(dbUSer.getRole());
		registerDto.setUserRole(dbUSer.getUserId());
		registerDto.setAuthorities(dbUSer.getAuthorities());

		return registerDto;
	}

	@Override
	public RoleDto addRole(RoleDto roleDto) {

		Role role = new Role();
		role.setName(roleDto.getName());

		List<Privilege> privileges = new ArrayList<>();

		for (Long privilagesIds : roleDto.getPrivilagesIds()) {
			Privilege privilege = privilegeRepository.findById(privilagesIds)
					.orElseThrow(() -> new IllegalArgumentException("Invalid permission ID: " + privilagesIds));
			privileges.add(privilege);
		}

		role.setPrivileges(privileges);
		Role savedRole = roleRepository.save(role);

		roleDto.setId(savedRole.getId());
		roleDto.setName(savedRole.getName());
		return roleDto;
	}

	@Override
	public Privilege addPrivilege(String name) {

		Privilege privilege = new Privilege();
		privilege.setName(name);
		Privilege savedPrivilege = privilegeRepository.save(privilege);

		return savedPrivilege;
	}

	@Override
	public RoleDto updateRole(RoleDto roleDto, Long roleId) {

		Role role = roleRepository.findById(roleId).get();

		role.setName(roleDto.getName());

		List<Privilege> privileges = new ArrayList<>();

		for (Long privilagesIds : roleDto.getPrivilagesIds()) {
			Privilege privilege = privilegeRepository.findById(privilagesIds)
					.orElseThrow(() -> new IllegalArgumentException("Invalid permission ID: " + privilagesIds));
			privileges.add(privilege);
		}

		role.setPrivileges(privileges);
		Role savedRole = roleRepository.save(role);

		roleDto.setId(savedRole.getId());
		roleDto.setName(savedRole.getName());
		return roleDto;
	}

	@Override
	public String getRoleById(Long id) {

		Role role = roleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Role not found with id ", null, id));
		Collection<Privilege> privileges = role.getPrivileges();
		List<String> privilageName = privileges.stream().map(s -> s.getName()).collect(Collectors.toList());

		return "role name : " + role.getName() + "\n" + "privileges : " + privilageName;
	}

	@Override
	public RegisterDto updateUser(RegisterDto registerDto, Long id) {

		User dbuser = userRepository.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("user not found with :" + id));

		dbuser.setName(registerDto.getName());
		dbuser.setUserName(registerDto.getUserName());
		dbuser.setEmail(registerDto.getEmail());
		dbuser.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		dbuser.setRole(registerDto.getRoles());

		Role userRole = roleRepository.findByName(registerDto.getRoles()).get();

		// list of all privileges objects under selected role.
		Collection<Privilege> userPrivileges = userRole.getPrivileges();

		Set<Role> setRoles = new HashSet<>();

		setRoles.add(userRole);
		dbuser.setRoles(setRoles);
		dbuser.setUserId(userRole.getId());

		List<String> privilages = userPrivileges.stream().map(s -> s.getName()).collect(Collectors.toList());

		dbuser.setAuthorities(privilages);

		User savedUser = userRepository.save(dbuser);

		registerDto.setId(savedUser.getId());
		registerDto.setName(savedUser.getName());
		registerDto.setUserName(savedUser.getUserName());
		registerDto.setEmail(savedUser.getEmail());
		registerDto.setPassword(savedUser.getPassword());
		registerDto.setRoles(savedUser.getRole());
		registerDto.setUserRole(savedUser.getUserId());
		registerDto.setAuthorities(savedUser.getAuthorities());

		return registerDto;
	}

	@Override
	public RegisterDto addUser(RegisterDto registerDto) {

		// Check weather user exist with same user name and email
		if (userRepository.existsByUserName(registerDto.getUserName())) {
			throw new EmployeeApiException(HttpStatus.BAD_REQUEST, "Username is already exists");
		}

		// Check weather user exist with same email
		if (userRepository.existsByEmail(registerDto.getEmail())) {
			throw new EmployeeApiException(HttpStatus.BAD_REQUEST, "Email is already exists");
		}

		User user = new User();
		user.setName(registerDto.getName());
		user.setUserName(registerDto.getUserName());
		user.setEmail(registerDto.getEmail());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		// set the role By default new Registered user is assigned with role "ROLE_USER"
		Role userRole = roleRepository.findByName(registerDto.getRoles()).get();

		// list of all privileges objects under selected role.
		Collection<Privilege> userPrivileges = userRole.getPrivileges();

		Set<Role> roles = new HashSet<>();

		roles.add(userRole);
		user.setRoles(roles);
		user.setUserId(userRole.getId());
		user.setRole(registerDto.getRoles());

		List<String> privilages = userPrivileges.stream().map(s -> s.getName()).collect(Collectors.toList());

		user.setAuthorities(privilages);

		User dbUSer = userRepository.save(user);

		registerDto.setId(dbUSer.getId());
		registerDto.setName(dbUSer.getName());
		registerDto.setUserName(dbUSer.getUserName());
		registerDto.setEmail(dbUSer.getEmail());
		registerDto.setPassword(dbUSer.getPassword());
		registerDto.setRoles(dbUSer.getRole());
		registerDto.setUserRole(dbUSer.getUserId());
		registerDto.setAuthorities(dbUSer.getAuthorities());

		return registerDto;
	}

	@Override
	public List<RegisterDto> getAllUsers() {

		List<User> users = userRepository.findAll();

		List<RegisterDto> registerDtos = new ArrayList<>();

		for (User user : users) {
			RegisterDto dto = new RegisterDto();
			dto.setId(user.getId());
			dto.setName(user.getName());
			dto.setEmail(user.getEmail());
			dto.setUserName(user.getUserName());
			dto.setPassword(user.getPassword());
			dto.setUserRole(user.getUserId());
			dto.setAuthorities(user.getAuthorities());
			dto.setRoles(user.getRole());

			registerDtos.add(dto);
		}

		return registerDtos;
	}

	@Override
	public RegisterDto getUserById(Long id) {

		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("user not found with id ", "", id));
		RegisterDto dto = new RegisterDto();

		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setUserName(user.getUserName());
		dto.setEmail(user.getEmail());
		dto.setAuthorities(user.getAuthorities());
		dto.setPassword(user.getPassword());
		dto.setRoles(user.getRole());
		dto.setUserRole(user.getUserId());

		return dto;
	}

	@Override
	public void deleteUserById(Long id) {
		
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User is", " ",id));
		userRepository.delete(user);
		
	}

}

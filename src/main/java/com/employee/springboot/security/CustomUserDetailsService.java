package com.employee.springboot.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.employee.springboot.entity.Privilege;
import com.employee.springboot.entity.Role;
import com.employee.springboot.entity.User;
import com.employee.springboot.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userNameOrEmail) throws UsernameNotFoundException {

		try {
			final User user = userRepository.findByUserNameOrEmail(userNameOrEmail, userNameOrEmail).get();
			if (user == null) {
				throw new UsernameNotFoundException("No user found with username: " + userNameOrEmail);
			}
	
			org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
					user.getEmail(), user.getPassword(), true, true, true, true, getAuthorities(user.getRoles()));
			return userDetails;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	// UTILITY
	private final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
		final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
			for (Privilege privilege : role.getPrivileges()) {
				authorities.add(new SimpleGrantedAuthority(privilege.getName()));
			}
		}
		return authorities;
	}

}
package com.employee.springboot.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.employee.springboot.exception.EmployeeApiException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	// This class is used to generate Jwt token.

	// Retrieving properties from application.properties
	@Value("${app.jwt-secret}")
	private String jwtSecret;

	@Value("${app-jwt-expiration-milliseconds}")
	private long jwtExpirationDate;

	// generate token
	// authentication object consisting of username,password, granted authorities
	
	public String generateToken(Authentication authentication) {
		// getName() method is used to get userName or email from authentication object
		String userName = authentication.getName();

		// Set expiration time to the token
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

		String token = Jwts.builder().setSubject(userName).setHeaderParam("type", "JWT").setIssuedAt(new Date())
				.setExpiration(expireDate).signWith(key()).compact();
		return token;
	}

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}

	// get username from jwt
	// This method is used to claim-(read or parse) Jwt details by providing 2
	// inputs 1.secret key 2.token
	// At the time of reading or parsing signature is only verified first.
	// If signature is valid then only remaining will be read(header,payload)
	public String getUserName(String token) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
		String userName = claims.getSubject();
		return userName;
	}

	// Validate JWT token
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
			return true;
		} catch (MalformedJwtException ex) {
			throw new EmployeeApiException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			throw new EmployeeApiException(HttpStatus.BAD_REQUEST, "Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			throw new EmployeeApiException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			throw new EmployeeApiException(HttpStatus.BAD_REQUEST, "JWT claims string is empired");
		}
	}

}
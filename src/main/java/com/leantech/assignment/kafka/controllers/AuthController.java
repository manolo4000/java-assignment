package com.leantech.assignment.kafka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.leantech.assignment.kafka.models.JwtRequest;
import com.leantech.assignment.kafka.models.JwtResponse;
import com.leantech.assignment.kafka.services.JWTUtilityService;
import com.leantech.assignment.kafka.services.UserService;

@RestController
public class AuthController {
	
	@Autowired
	private JWTUtilityService jwtUtility;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/auth")
	public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword(), null));
		}catch (BadCredentialsException e) {
			throw new Exception("Invalid Credentials", e);
		}
		
		final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
		
		final String token = jwtUtility.generateToken(userDetails);
		
		
		return new JwtResponse(token);
		
	}
}

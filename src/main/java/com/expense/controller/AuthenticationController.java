package com.expense.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.expense.entity.User;
import com.expense.service.CustomUserDetailService;
import com.expense.service.JwtService;

@RestController
public class AuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@PostMapping("/login")
	public ResponseEntity<String> authenticateUser(@RequestBody User user ){
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
			 UserDetails  userDetails=customUserDetailService.loadUserByUsername(user.getUsername());
			 User loggedInUser = (User) customUserDetailService.loadUserEntityByUsername(user.getUsername()); // Use new method
			 Long userId=loggedInUser.getId();
	            String token = jwtService.generateToken(userDetails,userId); // Pass userId to token
	            return ResponseEntity.ok(token)	;	}catch (BadCredentialsException e) {
            // Return 401 Unauthorized if the credentials are invalid
		return ResponseEntity.ok("Error");
	}

}
}

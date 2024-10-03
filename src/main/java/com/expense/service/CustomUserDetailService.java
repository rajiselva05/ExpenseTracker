package com.expense.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.expense.entity.User;
import com.expense.repository.UserRepository;


@Service
public class CustomUserDetailService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;
		@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
			User user=userRepository.findByUsername(username);
			if(user==null) {
				throw new UsernameNotFoundException("USername is not found");
			}
			return new org.springframework.security.core.userdetails.User(
					user.getUsername(),user.getPassword(), new ArrayList<>());
}
		public User loadUserEntityByUsername(String username) {
	        return userRepository.findByUsername(username);
	    }
}

package com.leantech.assignment.kafka.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.leantech.assignment.kafka.dao.UsersDAO;
import com.leantech.assignment.kafka.models.UserPrincipal;
import com.leantech.assignment.kafka.models.Users;

@Service
public class UserService implements UserDetailsService {
	
	@Autowired
	private UsersDAO usersDAO;
	
	@Autowired
	private static BCryptPasswordEncoder encoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users users = usersDAO.findByUsername(username);
		if(users==null) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
 
		return new UserPrincipal(users);
		
	}
	
	public void secureSave(Users user) {
		
		String EncryptedPass = encoder.encode(user.getPassword()); 
		user.setPassword(EncryptedPass);
		
		usersDAO.save(user);
	}

}

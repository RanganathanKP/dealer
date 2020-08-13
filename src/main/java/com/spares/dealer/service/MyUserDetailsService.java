package com.spares.dealer.service;

import com.spares.dealer.entity.MyUserDetails;
import com.spares.dealer.entity.UserEntity;
import com.spares.dealer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		Optional<UserEntity> user = userRepo.findByUserName(name);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + name));
        return user.map(MyUserDetails::new).get();
	}

	
}
package com.example.demo.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	@Autowired
	CourseRepository courseRepository;
	
	public UserEntity createOrUpdateUser(UserEntity newUser) {		
			return userRepository.save(newUser);			
	}
}

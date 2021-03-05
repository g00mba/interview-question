package com.backbase.interview.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class UserService. manages the repository of users
 */
@Service
public class UserService {

	/** The user repository. */
	@Autowired
	UserRepository userRepository;

	/** The course repository. */
	@Autowired
	CourseRepository courseRepository;

	/**
	 * Creates the user.
	 *
	 * @param newUser
	 * @return the user entity
	 */
	public UserEntity createOrUpdateUser(UserEntity newUser) {
		newUser.getCourse().setAvailability(newUser.getCourse().getAvailability() - 1);
		return userRepository.save(newUser);
	}

	/**
	 * Find by name and course.
	 *
	 * @param name
	 * @param course the course
	 * @return Optional UserEntity
	 */
	public Optional<UserEntity> findByNameAndCourse(String name, CourseEntity course) {
		return userRepository.findByNameAndCourse(name, course);
	}

	/**
	 * Delete user.
	 *
	 * @param user
	 */
	public void deleteUser(UserEntity user) {
		user.getCourse().setAvailability(user.getCourse().getAvailability() + 1);
		userRepository.delete(user);
	}
}

package com.example.demo.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The Class UserServiceTest. validates that the service layer is functioning
 * correctly for user entities
 */
@ExtendWith(SpringExtension.class)
class UserServiceTest {

	/** The course repository. */
	@Mock
	CourseRepository courseRepository;

	/** The user repository. */
	@Mock
	UserRepository userRepository;

	/** The course service. */
	@InjectMocks
	CourseService courseService;

	/** The user service. */
	@InjectMocks
	UserService userService;

	/** The test course. */
	CourseEntity testCourse = new CourseEntity();

	/** The persisted course. */
	CourseEntity persistedCourse = new CourseEntity();

	/** The test user. */
	UserEntity testUser = new UserEntity();

	/** The persisted user. */
	UserEntity persistedUser = new UserEntity();

	/**
	 * Setup.
	 *
	 * @throws RecordNotFoundException the record not found exception
	 */
	@BeforeEach
	void setup() throws RecordNotFoundException {
		testCourse.setId(1L);
		testCourse.setTitle("test");
		testCourse.setCapacity(10);
		testCourse.setAvailability(10);
		testCourse.setStartDate(LocalDate.parse("2020-01-20"));
		testCourse.setEndDate(LocalDate.parse("2020-01-29"));

		when(courseRepository.save(testCourse)).thenReturn(testCourse);
		persistedCourse = courseService.createOrUpdateCourse(testCourse);

		testUser.setName("stan");
		testUser.setCourse(persistedCourse);
		testUser.setRegistrationDate(LocalDate.of(2021, 01, 20));
		persistedUser = testUser;
		persistedUser.setCourse(persistedCourse);
	}

	/**
	 * Validates that a user can be successfully persisted
	 */
	@Test
	void testCreateUser() {
		when(userRepository.save(testUser)).thenReturn(persistedUser);
		UserEntity response = userService.createOrUpdateUser(testUser);
		assertEquals(testUser.getId(), response.getId());
		assertEquals(testUser.getName(), response.getName());
		assertEquals(testUser.getRegistrationDate(), response.getRegistrationDate());
		assertEquals(persistedCourse, response.getCourse());

	}

	/**
	 * Validates that a user can be found by passing the name and course
	 */
	@Test
	void testFindByNameAndCourse() {
		when(userRepository.findByNameAndCourse("stan", persistedCourse)).thenReturn(Optional.of(persistedUser));
		Optional<UserEntity> response = userService.findByNameAndCourse(testUser.getName(), persistedCourse);
		assertEquals(true, response.isPresent());
		assertEquals(testUser.getName(), response.get().getName());
		assertEquals(testUser.getRegistrationDate(), response.get().getRegistrationDate());
		assertEquals(testUser.getCancelDate(), response.get().getCancelDate());
		assertEquals(persistedCourse, response.get().getCourse());
	}

	/**
	 * validates that a user can be deleted successfully
	 */
	@Test
	void testDeleteUser() {
		Optional<UserEntity> optionalUser = Optional.of(persistedUser);
		when(userRepository.findById(persistedUser.getId())).thenReturn(optionalUser);

		userService.deleteUser(persistedUser);
		verify(userRepository, times(1)).delete(persistedUser);
	}
}

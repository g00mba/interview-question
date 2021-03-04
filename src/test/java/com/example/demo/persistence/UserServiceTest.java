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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

	@Mock
	CourseRepository courseRepository;

	@Mock
	UserRepository userRepository;

	@InjectMocks
	CourseService courseService;

	@InjectMocks
	UserService userService;

	CourseEntity testCourse = new CourseEntity();
	
	CourseEntity persistedCourse = new CourseEntity();
	
	UserEntity testUser = new UserEntity();
	
	UserEntity persistedUser = new UserEntity();
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

	@Test
	void testCreateUser() {
		when(userRepository.save(testUser)).thenReturn(persistedUser);
		UserEntity response = userService.createOrUpdateUser(testUser);
		assertEquals(testUser.getId(), response.getId());
		assertEquals(testUser.getName(), response.getName());
		assertEquals(testUser.getRegistrationDate(), response.getRegistrationDate());
		assertEquals(persistedCourse, response.getCourse());
		
	}
	
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
	
	@Test
	void testDeleteUser( ) {
		Optional<UserEntity> optionalUser = Optional.of(persistedUser);
		when(userRepository.findById(persistedUser.getId())).thenReturn(optionalUser);
		
		userService.deleteUser(persistedUser);
		verify(userRepository,times(1)).delete(persistedUser);
	}
}

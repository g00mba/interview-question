package com.example.demo.persistence;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserEntityTest {
	
	@Autowired
	private TestEntityManager entityManager;
	
	private static CourseEntity course = new CourseEntity();
	
	private static UserEntity user = new UserEntity();;
	
	@BeforeEach	
	public void setup() {
		course.setAvailability(10);
		course.setCapacity(10);
		course.setTitle("test");
		course.setStartDate(LocalDate.of(2021, 01, 10));
		course.setEndDate(LocalDate.of(2021, 10, 10));
		course = entityManager.persist(course);

		user.setName("stan");
		user.setRegistrationDate(LocalDate.of(2021, 01, 02));
		user.setCourse(course);
		
	}
	
	@Test
	void testUserEntity() {
		UserEntity persistedUser = entityManager.persist(user);
		assertNotNull(persistedUser.getId());
		assertEquals(user.getName(), persistedUser.getName());
		assertEquals(user.getRegistrationDate(), persistedUser.getRegistrationDate());
		assertEquals(user.getCancelDate(), persistedUser.getCancelDate());
		assertEquals(user.getCourse(), course);
	}

}

package com.backbase.interview.persistence.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * The Class CourseEntityTest. validates the persistence Layer for courses
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class CourseEntityTest {

	/** The entity manager. */
	@Autowired
	private TestEntityManager entityManager;

	/** The correct course. */
	private static CourseEntity correctCourse;

	/**
	 * Setup.
	 */
	@BeforeAll
	public static void setup() {
		correctCourse = new CourseEntity();
		correctCourse.setAvailability(10);
		correctCourse.setCapacity(10);
		correctCourse.setTitle("test");
		correctCourse.setStartDate(LocalDate.of(2021, 01, 10));
		correctCourse.setEndDate(LocalDate.of(2021, 10, 10));

	}

	/**
	 * Test valid course.
	 */
	@Test
	void testValidCourse() {
		CourseEntity persistedCourse = entityManager.persist(correctCourse);
		assertEquals(persistedCourse.getTitle(), correctCourse.getTitle());
		assertEquals(persistedCourse.getAvailability(), correctCourse.getAvailability());
		assertEquals(persistedCourse.getCapacity(), correctCourse.getCapacity());
		assertEquals(persistedCourse.getEndDate(), correctCourse.getEndDate());
		assertEquals(persistedCourse.getStartDate(), correctCourse.getStartDate());
		assertEquals(null, persistedCourse.getUsers());
		assertNotNull(persistedCourse.getId());
	}
}

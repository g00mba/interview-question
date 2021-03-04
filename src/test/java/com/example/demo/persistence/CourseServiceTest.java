package com.example.demo.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * The Class CourseServiceTest. validates the operation of the service layer for
 * courses
 */
@ExtendWith(SpringExtension.class)
class CourseServiceTest {

	/** The course repository. */
	@Mock
	CourseRepository courseRepository;

	/** The course service. */
	@InjectMocks
	CourseService courseService;

	/** The test course. */
	CourseEntity testCourse = new CourseEntity();

	/**
	 * Setup.
	 */
	@BeforeEach
	public void setup() {
		testCourse.setId(1L);
		testCourse.setTitle("test");
		testCourse.setCapacity(10);
		testCourse.setAvailability(10);
		testCourse.setStartDate(LocalDate.parse("2020-01-20"));
		testCourse.setEndDate(LocalDate.parse("2020-01-29"));
	}

	/**
	 * Create course validates that the course has been persisted.
	 *
	 * @throws RecordNotFoundException the record not found exception
	 */
	@Test
	void createCourseTest() throws RecordNotFoundException {
		when(courseRepository.save(testCourse)).thenReturn(testCourse);
		Optional<CourseEntity> persistedTestCourse = Optional.of(courseService.createOrUpdateCourse(testCourse));
		assertTrue(persistedTestCourse.isPresent());
	}

	/**
	 * Get course by id validates that an existing course can be found by Id
	 *
	 * @return the course by id test
	 * @throws RecordNotFoundException the record not found exception
	 */
	@Test
	void getCourseByIdTest() throws RecordNotFoundException {
		when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
		Optional<CourseEntity> foundTestCourse = courseService.getCourseById(testCourse.getId());
		assertTrue(foundTestCourse.isPresent());
	}

	/**
	 * Find courses by title test.
	 */
	@Test
	void findCoursesByTitleTest() {
		when(courseRepository.findByTitleLike("test")).thenReturn(List.of(Optional.of(testCourse)));
		List<Optional<CourseEntity>> foundTestCourseList = courseService.findCoursesByTitle("test");
		assertEquals(1, foundTestCourseList.size());
		assertEquals(foundTestCourseList.get(0), Optional.of(testCourse));
	}

	/**
	 * Gets the course by title test.
	 *
	 * @return the course by title test
	 * @throws RecordNotFoundException
	 */
	@Test
	void getCourseByTitleTest() throws RecordNotFoundException {
		when(courseRepository.findByTitle("test")).thenReturn(Optional.of(testCourse));
		Optional<CourseEntity> foundTestCourse = courseService.getCourseByTitle(testCourse.getTitle());
		assertEquals(foundTestCourse, Optional.of(testCourse));
	}

}

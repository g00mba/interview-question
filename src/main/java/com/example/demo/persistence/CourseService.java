package com.example.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The Class CourseService. handles the repository for courses
 */
@Service
public class CourseService {

	/** The course repository. */
	@Autowired
	CourseRepository courseRepository;

	/**
	 * Gets the course by id.
	 *
	 * @param id
	 * @return the course by id
	 * @throws RecordNotFoundException
	 */
	public Optional<CourseEntity> getCourseById(Long id) throws RecordNotFoundException {
		return courseRepository.findById(id);
	}

	/**
	 * Find courses by title.
	 *
	 * @param title
	 * @return the list of optional course entities
	 */
	public List<Optional<CourseEntity>> findCoursesByTitle(String title) {
		return courseRepository.findByTitleLike(title);
	}

	/**
	 * Gets the course by title.
	 *
	 * @param title
	 * @return the course by title
	 * @throws RecordNotFoundException
	 */
	public Optional<CourseEntity> getCourseByTitle(String title) throws RecordNotFoundException {
		return courseRepository.findByTitle(title);
	}

	/**
	 * Creates the or update course.
	 *
	 * @param course
	 * @return the course entity
	 * @throws RecordNotFoundException
	 */
	public CourseEntity createOrUpdateCourse(CourseEntity course) throws RecordNotFoundException {
		course.setAvailability(course.getCapacity());
		if (course.getId() != null) {
			Optional<CourseEntity> persistedCourse = courseRepository.findById(course.getId());

			if (persistedCourse.isPresent()) {
				return persistedCourse.get();
			}

			else
				return courseRepository.save(course);
		}

		else
			return courseRepository.save(course);
	}
}

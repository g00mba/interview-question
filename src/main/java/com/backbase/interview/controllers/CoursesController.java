package com.backbase.interview.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.backbase.interview.persistence.RecordNotFoundException;
import com.backbase.interview.persistence.entities.CourseEntity;
import com.backbase.interview.persistence.entities.UserEntity;
import com.backbase.interview.services.CourseService;
import com.backbase.interview.services.UserService;

/**
 * The Class CoursesController. this class handles all requests and constraint
 * rules for the project, the class is able to generate an endpoint for the following cases:
 * create a course and handle exceptions
 * add a user with controlled edge cases
 * deletes a user with edge cases
 */
@RestController
@RequestMapping("/courses")
public class CoursesController extends ResponseEntityExceptionHandler {

	/** The course service. */
	@Autowired
	CourseService courseService;

	/** The user service. */
	@Autowired
	UserService userService;

	/**
	 * Creates a course.
	 *
	 * @param course: the course represented as a CourseEntity
	 * @return the response entity as a persisted CourseEntity
	 * @throws ResponseStatusException
	 */
	@PostMapping
	public ResponseEntity<CourseEntity> createOrUpdateCourse(@RequestBody CourseEntity course)
			throws RecordNotFoundException {
		Optional<CourseEntity> persistedCourse = courseService
				.getCourseByTitle(course.getTitle().replaceAll("^\"|\"$", ""));
		if (persistedCourse.isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A course by that name already exists!");

		CourseEntity updated = courseService.createOrUpdateCourse(course);
		return new ResponseEntity<>(updated, new HttpHeaders(), HttpStatus.CREATED);
	}

	/**
	 * Find by title. given a title (as a string) finds a list of possible matches
	 *
	 * @param title
	 * @return the response entity represented as a CourseEntity
	 * @throws ResponseStatusException
	 */
	@GetMapping
	public ResponseEntity<List<Optional<CourseEntity>>> findByTitle(@RequestParam(name = "q") String title)
			throws RecordNotFoundException {
		List<Optional<CourseEntity>> course = courseService.findCoursesByTitle(title.replaceAll("^\"|\"$", ""));

		if (course.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no course found by that name!");

		return new ResponseEntity<>(course, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Find by id. Given a String containing the ID, returns the related course (if found)
	 *
	 * @param id
	 * @return the response entity
	 * @throws ResponseStatusException
	 */
	@GetMapping("/{id}")
	public ResponseEntity<CourseEntity> findById(@PathVariable Long id) throws RecordNotFoundException {
		Optional<CourseEntity> course = courseService.getCourseById(id);
		if (course.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the given course Id was not found");
		return new ResponseEntity<>(course.get(), new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Adds the user. given a user represented as a UserEntity and a valid course Id in the path
	 * the method will persist the user
	 *
	 * @param courseId as a String
	 * @param newUser as a UserEntity
	 * @return the response entity
	 * @throws ResponseStatusException
	 */
	@PostMapping("/{courseId}/add")
	public ResponseEntity<CourseEntity> addUser(@PathVariable Long courseId, @RequestBody UserEntity newUser)
			throws RecordNotFoundException {
		Optional<CourseEntity> course = courseService.getCourseById(courseId);

		if (course.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the given course Id was not found");

		Optional<UserEntity> persistedUser = userService.findByNameAndCourse(newUser.getName(), course.get());
		if (persistedUser.isPresent())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"a user with the same name is already enrolled in the course");

		if (course.get().getAvailability() < 1)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"this course has reached the maximum amount of users");

		if (newUser.getRegistrationDate().isAfter(course.get().getStartDate().minusDays(3)))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"the student tried to enroll after the enrollment period ended");

		newUser.setCourse(course.get());
		userService.createOrUpdateUser(newUser);
		return new ResponseEntity<>(course.get(), new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Removes the user. given a valid UserEntity in the body and a valid course id in the path,
	 * this endpoint removes the user.
	 *
	 * @param courseId as part of the url
	 * @param user as a UserEntity
	 * @return the response entity
	 * @throws ResponseStatusException
	 */
	@PostMapping("/{courseId}/remove")
	public ResponseEntity<CourseEntity> removeUser(@PathVariable Long courseId, @RequestBody UserEntity user)
			throws RecordNotFoundException {
		Optional<CourseEntity> course = courseService.getCourseById(courseId);

		if (course.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the given course Id was not found");

		Optional<UserEntity> persistedUser = userService.findByNameAndCourse(user.getName(), course.get());
		if (persistedUser.isPresent()) {
			if (user.getCancelDate().isAfter(course.get().getStartDate().minusDays(3)))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"the student tried to cancel subscription after the cancellation period ended");

			userService.deleteUser(persistedUser.get());
		} else
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "given user is not enrolled in course");

		return new ResponseEntity<>(course.get(), new HttpHeaders(), HttpStatus.OK);

	}
}

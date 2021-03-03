package com.example.demo.persistence;

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

@RestController
@RequestMapping("/courses")
public class CourseController extends ResponseEntityExceptionHandler {
	@Autowired
	CourseService courseService;

	@Autowired
	UserService userService;

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

	@GetMapping
	public ResponseEntity<List<Optional<CourseEntity>>> findByTitle(@RequestParam(name = "q") String title)
			throws RecordNotFoundException {
		List<Optional<CourseEntity>> course = courseService.findCoursesByTitle(title.replaceAll("^\"|\"$", ""));
		
		if (course.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no course found by that name!");
		
		return new ResponseEntity<>(course, new HttpHeaders(), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CourseEntity> findById(@PathVariable Long id) throws RecordNotFoundException {
		Optional<CourseEntity> course = courseService.getCourseById(id);
		if (course.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the given course Id was not found");
		return new ResponseEntity<>(course.get(), new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/{courseId}/add")
	public ResponseEntity<CourseEntity> addUser(@PathVariable Long courseId, @RequestBody UserEntity newUser)
			throws RecordNotFoundException {
		Optional<CourseEntity> course = courseService.getCourseById(courseId);
		
		if (course.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "the given course Id was not found");
		
		Optional<UserEntity> persistedUser = userService.findByNameAndCourse(newUser.getName(), course.get());
		if (persistedUser.isPresent()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "a user with the same name is already enrolled in the course");
		
		if (course.get().getAvailability() < 1)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "this course has reached the maximum amount of users");
		

		if ( newUser.getRegistrationDate().isAfter(course.get().getStartDate().minusDays(3)))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "the student tried to enroll after the enrollment period ended");

		newUser.setCourse(course.get());
		userService.createOrUpdateUser(newUser);
		return new ResponseEntity<>(course.get(), new HttpHeaders(), HttpStatus.OK);
	}
}

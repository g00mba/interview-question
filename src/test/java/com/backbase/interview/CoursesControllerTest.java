package com.backbase.interview;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.backbase.interview.persistence.CourseEntity;
import com.backbase.interview.persistence.CourseService;
import com.backbase.interview.persistence.UserEntity;
import com.backbase.interview.persistence.UserService;

/**
 * The Class CoursesControllerTest. Evaluates all the endpoints and its
 * behaviors
 */
@WebMvcTest
class CoursesControllerTest {

	/** The mvc. */
	@Autowired
	private MockMvc mvc;

	/** The course service. */
	@MockBean
	private CourseService courseService;

	/** The user service. */
	@MockBean
	private UserService userService;

	/** The user. */
	private UserEntity user;

	/** The course. */
	private CourseEntity course;

	/**
	 * Setup.
	 */
	@BeforeEach
	void setup() {
		user = new UserEntity("peter", LocalDate.of(2021, 02, 05));
		course = new CourseEntity("test", LocalDate.of(2021, 05, 10), LocalDate.of(2021, 05, 25), 10);
	}

	/**
	 * Create course call should return OK.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void createOrUpdateCourseShouldReturnOK() throws Exception {
		when(courseService.getCourseByTitle("test")).thenReturn(Optional.empty());
		when(courseService.createOrUpdateCourse(course)).thenReturn(course);

		String userJson = "{\r\n" + "  \"title\": \"test\",\r\n" + "  \"startDate\": \"2021-05-04\",\r\n"
				+ "  \"endDate\": \"2021-05-20\",\r\n" + "  \"capacity\": 10\r\n" + "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses").content(userJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}

	/**
	 * Create course call should return bad request.
	 *
	 * @throws Exception
	 */
	@Test
	void createOrUpdateCourseShouldReturnBadRequest() throws Exception {
		when(courseService.getCourseByTitle("test")).thenReturn(Optional.of(course));
		String userJson = "{\r\n" + "  \"title\": \"test\",\r\n" + "  \"startDate\": \"2021-05-04\",\r\n"
				+ "  \"endDate\": \"2021-05-20\",\r\n" + "  \"capacity\": 10\r\n" + "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses").content(userJson).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	/**
	 * Find by title call should return ok.
	 *
	 * @throws Exception
	 */
	@Test
	void findByTitleShouldReturnOk() throws Exception {
		when(courseService.findCoursesByTitle("test")).thenReturn(List.of(Optional.of(course)));
		mvc.perform(MockMvcRequestBuilders.get("/courses?q={title}", "test").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	/**
	 * Find by title call should return not found.
	 *
	 * @throws Exception
	 */
	@Test
	void findByTitleShouldReturnNotFound() throws Exception {
		when(courseService.findCoursesByTitle("test")).thenReturn(Collections.emptyList());
		mvc.perform(MockMvcRequestBuilders.get("/courses?q={title}", "test").accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());
	}

	/**
	 * Find by id call should return ok.
	 *
	 * @throws Exception
	 */
	@Test
	void findByIdShouldReturnOk() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
		mvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}", 1).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	/**
	 * Find by id call should return not found.
	 *
	 * @throws Exception
	 */
	@Test
	void findByIdShouldReturnNotFound() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.empty());
		mvc.perform(MockMvcRequestBuilders.get("/courses/{courseId}", 1).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());
	}

	/**
	 * Add user call should return not found when no course is present.
	 *
	 * @throws Exception
	 */
	@Test
	void addUserShouldReturnNotFoundWhenNoCourseIsPresent() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.empty());
		String addUserJson = "{\r\n" + "  \"registrationDate\": \"2021-04-29\",\r\n" + "  \"name\": \"timmy\"\r\n"
				+ "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/add", 1).content(addUserJson)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	/**
	 * Add user call should return bad request when user is already enrolled.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void addUserShouldReturnBadRequestWhenUserIsAlreadyEnrolled() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
		when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.of(user));
		String addUserJson = "{\r\n" + "  \"registrationDate\": \"2021-04-29\",\r\n" + "  \"name\": \"timmy\"\r\n"
				+ "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/add", 1).content(addUserJson)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Add user call should return bad request when course is full.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void addUserShouldReturnBadRequestWhenCourseIsFull() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
		when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.empty());
		course.setAvailability(0);
		String addUserJson = "{\r\n" + "  \"registrationDate\": \"2021-04-29\",\r\n" + "  \"name\": \"timmy\"\r\n"
				+ "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/add", 1).content(addUserJson)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Add user call should return bad request when late for registration.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void addUserShouldReturnBadRequestWhenLateForRegistration() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
		when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.empty());
		course.setStartDate(LocalDate.of(2021, 04, 30));
		course.setAvailability(1);
		String addUserJson = "{\r\n" + "  \"registrationDate\": \"2021-04-29\",\r\n" + "  \"name\": \"timmy\"\r\n"
				+ "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/add", 1).content(addUserJson)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Add user call should return ok.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void addUserShouldReturnOk() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
		when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.empty());
		course.setAvailability(1);
		String addUserJson = "{\r\n" + "  \"registrationDate\": \"2021-04-29\",\r\n" + "  \"name\": \"timmy\"\r\n"
				+ "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/add", 1).content(addUserJson)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	/**
	 * Remove user call should return not found when invalid course.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void removeUserShouldReturnNotFoundWhenInvalidCourse() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.empty());
		String deleteUserJson = "{\r\n" + "  \"cancelDate\": \"2021-05-01\",\r\n" + "  \"name\": \"timmy\"\r\n" + "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/remove", 1).content(deleteUserJson)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	/**
	 * Remove user call should return not found when not enrolled.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void removeUserShouldReturnNotFoundWhenNotEnrolled() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
		when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.empty());
		String deleteUserJson = "{\r\n" + "  \"cancelDate\": \"2021-05-01\",\r\n" + "  \"name\": \"timmy\"\r\n" + "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/remove", 1).content(deleteUserJson)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	/**
	 * Remove user call should return bad request when late for cancellation.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void removeUserShouldReturnBadRequestWhenLateForCancellation() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
		when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.of(user));
		course.setStartDate(LocalDate.of(2021, 05, 03));
		String deleteUserJson = "{\r\n" + "  \"cancelDate\": \"2021-05-02\",\r\n" + "  \"name\": \"timmy\"\r\n" + "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/remove", 1).content(deleteUserJson)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	/**
	 * Remove user call should return ok.
	 *
	 * @throws Exception the exception
	 */
	@Test
	void RemoveUserShouldReturnOk() throws Exception {
		when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
		when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.of(user));
		doNothing().when(userService).deleteUser(user);
		String deleteUserJson = "{\r\n" + "  \"cancelDate\": \"2021-05-02\",\r\n" + "  \"name\": \"timmy\"\r\n" + "}";
		mvc.perform(MockMvcRequestBuilders.post("/courses/{courseId}/remove", 1).content(deleteUserJson)
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}
}

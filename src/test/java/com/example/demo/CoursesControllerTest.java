package com.example.demo;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.persistence.CourseEntity;
import com.example.demo.persistence.CourseService;
import com.example.demo.persistence.RecordNotFoundException;
import com.example.demo.persistence.UserEntity;
import com.example.demo.persistence.UserService;



@WebMvcTest
class CoursesControllerTest {
 @Autowired
 private MockMvc mvc;

@MockBean
private CourseService courseService;

@MockBean
private UserService userService;

private UserEntity user;

private CourseEntity course;

@BeforeEach
void setup() {
	user = new UserEntity("peter",LocalDate.of(2021, 02, 05));
	course = new CourseEntity("test", LocalDate.of(2021, 05, 10), LocalDate.of(2021, 05,25),10);
}

@Test
 void createOrUpdateCourseShouldReturnOK() throws Exception {
	when(courseService.getCourseByTitle("test")).thenReturn(Optional.empty());
	when(courseService.createOrUpdateCourse(course)).thenReturn(course);

	String userJson = "{\r\n"
			+ "  \"title\": \"test\",\r\n"
			+ "  \"startDate\": \"2021-05-04\",\r\n"
			+ "  \"endDate\": \"2021-05-20\",\r\n"
			+ "  \"capacity\": 10\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses")
			.content(userJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isCreated());
}

@Test
void createOrUpdateCourseShouldReturnBadRequest() throws Exception {
	when(courseService.getCourseByTitle("test")).thenReturn(Optional.of(course));
	String userJson = "{\r\n"
			+ "  \"title\": \"test\",\r\n"
			+ "  \"startDate\": \"2021-05-04\",\r\n"
			+ "  \"endDate\": \"2021-05-20\",\r\n"
			+ "  \"capacity\": 10\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses")
			.content(userJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isBadRequest());
}

@Test
void findByTitleShouldReturnOk() throws Exception {
	when(courseService.findCoursesByTitle("test")).thenReturn(List.of(Optional.of(course)));
	mvc.perform(MockMvcRequestBuilders
			.get("/courses?q={title}","test")
		     .accept(MediaType.APPLICATION_JSON))
		     .andDo(print())
			.andExpect(status().isOk()
			);
}

@Test
void findByTitleShouldReturnNotFound() throws Exception {
	when(courseService.findCoursesByTitle("test")).thenReturn(Collections.emptyList());
	mvc.perform(MockMvcRequestBuilders
			.get("/courses?q={title}","test")
		     .accept(MediaType.APPLICATION_JSON))
		     .andDo(print())
			.andExpect(status().isNotFound()
			);
}

@Test
void findByIdShouldReturnOk() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
	mvc.perform(MockMvcRequestBuilders
			.get("/courses/{courseId}",1)
		     .accept(MediaType.APPLICATION_JSON))
		     .andDo(print())
			.andExpect(status().isOk()
			);
}

@Test
void findByIdShouldReturnNotFound() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.empty());
	mvc.perform(MockMvcRequestBuilders
			.get("/courses/{courseId}",1)
		     .accept(MediaType.APPLICATION_JSON))
		     .andDo(print())
			.andExpect(status().isNotFound()
			);
}

@Test
void addUserShouldReturnNotFoundWhenNoCourseIsPresent() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.empty());
	String addUserJson="{\r\n"
			+ "  \"registrationDate\": \"2021-04-29\",\r\n"
			+ "  \"name\": \"timmy\"\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses/{courseId}/add",1)
			.content(addUserJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isNotFound());	
}

@Test
void addUserShouldReturnBadRequestWhenUserIsAlreadyEnrolled() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
	when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.of(user));
	String addUserJson="{\r\n"
			+ "  \"registrationDate\": \"2021-04-29\",\r\n"
			+ "  \"name\": \"timmy\"\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses/{courseId}/add",1)
			.content(addUserJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isBadRequest());		
}

@Test
void addUserShouldReturnBadRequestWhenCourseIsFull() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
	when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.empty());
	course.setAvailability(0);
	String addUserJson="{\r\n"
			+ "  \"registrationDate\": \"2021-04-29\",\r\n"
			+ "  \"name\": \"timmy\"\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses/{courseId}/add",1)
			.content(addUserJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isBadRequest());		
}

@Test
void addUserShouldReturnBadRequestWhenLateForRegistration() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
	when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.empty());
	course.setStartDate(LocalDate.of(2021, 04, 30));
	course.setAvailability(1);
	String addUserJson="{\r\n"
			+ "  \"registrationDate\": \"2021-04-29\",\r\n"
			+ "  \"name\": \"timmy\"\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses/{courseId}/add",1)
			.content(addUserJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isBadRequest());
}

@Test
void addUserShouldReturnOk() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
	when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.empty());
	course.setAvailability(1);
	String addUserJson="{\r\n"
			+ "  \"registrationDate\": \"2021-04-29\",\r\n"
			+ "  \"name\": \"timmy\"\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses/{courseId}/add",1)
			.content(addUserJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isOk());
}

@Test
void removeUserShouldReturnNotFoundWhenInvalidCourse() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.empty());
	String deleteUserJson = "{\r\n"
			+ "  \"cancelDate\": \"2021-05-01\",\r\n"
			+ "  \"name\": \"timmy\"\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses/{courseId}/remove",1)
			.content(deleteUserJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isNotFound());						
}

@Test
void removeUserShouldReturnNotFoundtWhenNotEnrolled() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
	when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.empty());
	String deleteUserJson = "{\r\n"
			+ "  \"cancelDate\": \"2021-05-01\",\r\n"
			+ "  \"name\": \"timmy\"\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses/{courseId}/remove",1)
			.content(deleteUserJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isNotFound());	
}

@Test
void removeUserShouldReturnBadRequestWhenLateForCancellation() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
	when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.of(user));
	course.setStartDate(LocalDate.of(2021,05, 03));
	String deleteUserJson = "{\r\n"
			+ "  \"cancelDate\": \"2021-05-02\",\r\n"
			+ "  \"name\": \"timmy\"\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses/{courseId}/remove",1)
			.content(deleteUserJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isBadRequest());	
}

@Test
void RemoveUserShouldReturnOk() throws Exception {
	when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));
	when(userService.findByNameAndCourse("timmy", course)).thenReturn(Optional.of(user));
	doNothing().when(userService).deleteUser(user);
	String deleteUserJson = "{\r\n"
			+ "  \"cancelDate\": \"2021-05-02\",\r\n"
			+ "  \"name\": \"timmy\"\r\n"
			+ "}";
	mvc.perform(MockMvcRequestBuilders
			.post("/courses/{courseId}/remove",1)
			.content(deleteUserJson)
		      .contentType(MediaType.APPLICATION_JSON)
		      .accept(MediaType.APPLICATION_JSON))
		      .andExpect(status().isOk());	
	
}
}

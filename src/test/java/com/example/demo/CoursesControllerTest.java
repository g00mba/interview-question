package com.example.demo;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.persistence.CourseEntity;
import com.example.demo.persistence.CourseService;
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


private UserEntity user = new UserEntity("peter",LocalDate.of(2021, 02, 05));


private CourseEntity course = new CourseEntity("test", LocalDate.of(2021, 05, 10), LocalDate.of(2021, 05,25),10);

@Test
 void testCreateOrUpdateCourseShouldReturnOK() throws Exception {
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

}

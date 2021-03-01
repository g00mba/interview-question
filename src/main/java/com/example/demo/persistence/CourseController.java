package com.example.demo.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses")
public class CourseController {
	@Autowired
	CourseService service;
	
	@PostMapping
	public ResponseEntity<CourseEntity> createOrUpdateCourse (@RequestBody CourseEntity course)
		throws RecordNotFoundException{
		CourseEntity updated = service.createOrUpdateCourse(course);
		return new ResponseEntity<CourseEntity>(updated, new HttpHeaders(),HttpStatus.CREATED);
	}

}

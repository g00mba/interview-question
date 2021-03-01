package com.example.demo.persistence;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
	
	@Autowired
	CourseRepository courseRepository;
	
	public CourseEntity getCourseById(Long id) throws RecordNotFoundException {
		Optional<CourseEntity> course = courseRepository.findById(id);
		if (course.isPresent()) return course.get();
		else throw new RecordNotFoundException("no course found for given Id");				
	}
	
	public CourseEntity createOrUpdateCourse(CourseEntity course) throws RecordNotFoundException {
	
		Optional<CourseEntity> persistedCourse = courseRepository.findById(course.getId());
		
		if (persistedCourse.isPresent()) {
			CourseEntity updatedCourse = persistedCourse.get();
			updatedCourse.setCapacity(course.getCapacity());
			updatedCourse.setStartDate(course.getStartDate());
			updatedCourse.setEndDate(course.getEndDate());
			updatedCourse.setTitle(course.getTitle());
			
			updatedCourse = courseRepository.save(updatedCourse);
			
			return updatedCourse;
		}
		
		else return courseRepository.save(course);		
	}
}

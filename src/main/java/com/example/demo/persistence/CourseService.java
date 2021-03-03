package com.example.demo.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseService {
	
	@Autowired
	CourseRepository courseRepository;
	
	public Optional<CourseEntity> getCourseById(Long id) throws RecordNotFoundException {
		return courseRepository.findById(id);			
	}
	
	public List<Optional<CourseEntity>> findCoursesByTitle(String title) {
		return courseRepository.findByTitleLike(title);
	}
	
	public Optional<CourseEntity> getCourseByTitle(String title) throws RecordNotFoundException{
		return Optional.ofNullable(courseRepository.findByTitle(title));
	}
	
	public CourseEntity createOrUpdateCourse(CourseEntity course) throws RecordNotFoundException {
		course.setAvailability(course.getCapacity());
		if (course.getId() != null) {
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
		
		else return courseRepository.save(course);		
	}
}

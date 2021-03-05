package com.backbase.interview.persistence.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backbase.interview.persistence.entities.CourseEntity;
import com.backbase.interview.persistence.entities.UserEntity;

/**
 * The Interface UserRepository. default implementation of JpaRepository for
 * handling course data
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	/**
	 * Find by name and course.
	 *
	 * @param name
	 * @param course
	 * @return Optional UserEntity
	 */
	Optional<UserEntity> findByNameAndCourse(String name, CourseEntity course);

}

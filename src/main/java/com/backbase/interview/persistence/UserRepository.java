package com.backbase.interview.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Interface UserRepository.
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

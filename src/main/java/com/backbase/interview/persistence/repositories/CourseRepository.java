package com.backbase.interview.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backbase.interview.persistence.entities.CourseEntity;

/**
 * The Interface CourseRepository. default implementation of JpaRepository for
 * handling course data
 */
@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

	/**
	 * Find by title.
	 *
	 * @param title the title
	 * @return optional course Entity
	 */
	Optional<CourseEntity> findByTitle(String title);

	/**
	 * Find by title like.
	 *
	 * @param title the title
	 * @return List of optional course entities
	 */
	List<Optional<CourseEntity>> findByTitleLike(String title);

}

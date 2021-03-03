package com.example.demo.persistence;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
	CourseEntity findByTitle (String title);
	List<Optional<CourseEntity>> findByTitleLike (String title);

}

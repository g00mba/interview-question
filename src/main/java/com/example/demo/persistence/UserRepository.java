package com.example.demo.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	Optional<UserEntity> findByNameAndCourse(String  name, CourseEntity course);

}

package com.backbase.interview.persistence.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The Class CourseEntity. contains all elements pertaining the course, one to
 * many relationship with UserEntity
 */
@Entity

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new course entity.
 *
 * @param title
 * @param startDate
 * @param endDate
 * @param capacity
 */
@RequiredArgsConstructor

/**
 * Instantiates a new course entity.
 */
@NoArgsConstructor
@Table(name = "courses")
public class CourseEntity {

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	/** The title. */
	@NonNull
	@Column(name = "title", nullable = false, length = 350, unique = true)
	private String title;

	/** The start date. */
	@NonNull
	@Column(name = "start_date", nullable = false)
	private LocalDate startDate;

	/** The end date. */
	@NonNull
	@Column(name = "end_date", nullable = false)
	private LocalDate endDate;

	/** The capacity. */
	@NonNull
	@Column(name = "capacity", nullable = false)
	private Integer capacity;

	/** The availability. */
	@JsonProperty("remaining")
	@Column(name = "availability")
	private Integer availability;

	/** The users as a list of entities */
	@JsonProperty("participants")
	@JsonInclude(Include.NON_NULL)
	@OneToMany(mappedBy = "course")
	private List<UserEntity> users;
}

package com.backbase.interview.persistence;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * The Class UserEntity. database representation of a user, has a many to one
 * relationship with CourseEntity
 */
@Entity

/**
 * To string.
 *
 * @return the java.lang. string
 */
@Data

/**
 * Instantiates a new user entity.
 *
 * @param course
 * @param id
 * @param name
 * @param registrationDate
 * @param cancelDate
 */
@AllArgsConstructor

/**
 * Instantiates a new user entity.
 */
@NoArgsConstructor

/**
 * Instantiates a new user entity.
 *
 * @param name
 * @param registrationDate
 */
@RequiredArgsConstructor
@Table(name = "TBL_USERS", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "course" }) })
public class UserEntity {

	/** The course. */
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "course", nullable = false)
	private CourseEntity course;

	/** The id. */
	@Id
	@JsonIgnore
	@GeneratedValue
	private Long id;

	/** The name. */
	@NonNull
	@Column(name = "name", nullable = false, length = 350)
	private String name;

	/** The registration date. */
	@NonNull
	@Column(name = "registrationDate", nullable = false)
	private LocalDate registrationDate;

	/** The cancel date. */
	@Column(name = "cancelDate")
	@JsonInclude(Include.NON_NULL)
	private LocalDate cancelDate;
}

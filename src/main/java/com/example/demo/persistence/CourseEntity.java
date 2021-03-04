package com.example.demo.persistence;

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

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "courses")
public class CourseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	@NonNull
	@Column(name = "title", nullable=false, length=350, unique = true)
	private String title;
	
	@NonNull
	@Column(name = "start_date",nullable=false)
	private LocalDate startDate;
	
	@NonNull
	@Column(name = "end_date", nullable=false)
	private LocalDate endDate;
	
	@NonNull
	@Column (name = "capacity", nullable=false)
	private Integer capacity;
	
	@JsonProperty("remaining")
	@Column (name = "availability")
	private Integer availability;
	
    @JsonProperty("participants")
	@JsonInclude(Include.NON_NULL)
	@OneToMany(mappedBy = "course")
	private List<UserEntity> users;
}

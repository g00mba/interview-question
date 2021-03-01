package com.example.demo.persistence;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@Table(name = "TBL_COURSES")
public class CourseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long id;
	
	@NonNull
	@Column(name = "title", nullable=false, length=350, unique = true)
	private String title;
	
	@NonNull
	@Column(name = "startDate",nullable=false)
	private Date startDate;
	
	@NonNull
	@Column(name = "endDate", nullable=false)
	private Date endDate;
	
	@NonNull
	@Column (name = "capacity", nullable=false)
	private Integer capacity;
	
	@JsonProperty("remaining")
	@Column (name = "availability")
	private Integer availability;
	

    @JsonInclude(Include.NON_NULL)
	@OneToMany(mappedBy = "course")
	private Set<UserEntity> users;
}

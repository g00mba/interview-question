package com.example.demo.persistence;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@Table(name = "TBL_COURSES")
public class CourseEntity {

	@Id
	@GeneratedValue
	private Long id;
	
	@NonNull
	@Column(name = "title", nullable=false, length=350)
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
	
	@Column (name = "availability")
	private Integer availability;
	
	@OneToMany(mappedBy = "course")
	private Set<UserEntity> users;
}

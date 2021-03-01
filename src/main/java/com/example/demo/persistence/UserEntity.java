package com.example.demo.persistence;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NonNull;

@Entity
@Data
@Table(name="TBL_USERS")
public class UserEntity {
	
	@ManyToOne
	@JoinColumn(name = "course_id", nullable = false)
	private CourseEntity course;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@NonNull
	@Column(name ="name", nullable=false)
	private String name;
	
	@NonNull
	@Column(name = "registrationDate", nullable=false)
	private Date registrationDate;
	
	@Column(name = "cancelDate")
	private Date cancelDate;
}

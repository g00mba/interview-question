package com.example.demo.persistence;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
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

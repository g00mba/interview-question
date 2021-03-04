package com.example.demo.persistence;

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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name="TBL_USERS", uniqueConstraints={
	    @UniqueConstraint(columnNames = {"name", "course"})
	}) 
public class UserEntity {

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "course", nullable = false)
	private CourseEntity course;
	
	@Id
	@JsonIgnore
	@GeneratedValue
	private Long id;
	
	@NonNull
	@Column(name ="name", nullable=false, length=350)
	private String name;
	
	@NonNull
	@Column(name = "registrationDate", nullable=false)
	private LocalDate registrationDate;
	
	@Column(name = "cancelDate")
    @JsonInclude(Include.NON_NULL)
	private LocalDate cancelDate;
}

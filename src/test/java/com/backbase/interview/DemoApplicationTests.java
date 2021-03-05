package com.backbase.interview;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.backbase.interview.CoursesController;

/**
 * The Class DemoApplicationTests.
 */
@SpringBootTest
class DemoApplicationTests {

	/** The controller. */
	@Autowired
	private CoursesController controller;

	/**
	 * Context loads.
	 */
	@Test
	void contextLoads() {
		assertThat(controller).isNotNull();
	}

}

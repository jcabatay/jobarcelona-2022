package com.ascii274.jobarcelona22;

import com.ascii274.jobarcelona22.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ApplicationTest {

	@Autowired
	private UserController userController;

	@Test
	public void contextLoads() throws Exception{
		assertThat(userController).isNotNull();
	}

}

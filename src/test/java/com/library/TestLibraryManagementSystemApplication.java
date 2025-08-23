package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class TestLibraryManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(LibraryManagementSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

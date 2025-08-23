package com.library;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {LibraryManagementSystemApplication.class}) // Only load main app, not TestcontainersConfiguration
class LibraryManagementSystemApplicationTests {

	@Test
	void contextLoads() {
	}
}
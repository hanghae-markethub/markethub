
package org.hanghae.markethub.domain.user;

import org.hanghae.markethub.domain.user.entity.User;
import org.hanghae.markethub.global.constant.Role;
import org.hanghae.markethub.global.constant.Status;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTest {

	@Test
	void createUser() {
		User user = User.builder()
				.id(1L)
				.email("1234@naver.com")
				.password("1234")
				.name("lee")
				.phone("010-1234")
				.address("서울시")
				.role(Role.ADMIN)
				.status(Status.EXIST)
				.build();

		System.out.println(user.getName());
	}
}

//package org.hanghae.markethub.domain.user;
//
//import org.hanghae.markethub.domain.user.entity.User;
//import org.hanghae.markethub.global.constant.Role;
//import org.hanghae.markethub.global.constant.Status;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class UserTest {
//
//	@Test
//	void createUser() {
//		User user = User.builder()
//				.id(1L)
//				.email("1234@naver.com")
//				.password("1234")
//				.name("lee")
//				.phone("010-1234")
//				.address("서울시")
//				.role(Role.ADMIN)
//				.status(Status.EXIST)
//				.build();
//
//		System.out.println(user.getName());
//	}
//}

package org.hanghae.markethub.domain.cart.repository;

import org.hanghae.markethub.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
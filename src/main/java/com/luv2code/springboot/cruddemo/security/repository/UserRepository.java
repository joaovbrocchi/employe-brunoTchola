package com.luv2code.springboot.cruddemo.security.repository;

import com.luv2code.springboot.cruddemo.security.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);


}

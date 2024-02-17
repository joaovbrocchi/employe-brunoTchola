package com.luv2code.springboot.cruddemo.security.DTO;

import com.luv2code.springboot.cruddemo.security.entity.user.UserRole;

public record RegisterDTO(String email, String password, UserRole role) {
}
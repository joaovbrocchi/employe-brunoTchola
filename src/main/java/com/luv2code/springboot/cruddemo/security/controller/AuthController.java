package com.luv2code.springboot.cruddemo.security.controller;

import com.luv2code.springboot.cruddemo.security.DTO.AuthenticationDTO;
import com.luv2code.springboot.cruddemo.security.DTO.LoginResponseDTO;
import com.luv2code.springboot.cruddemo.security.DTO.RegisterDTO;
import com.luv2code.springboot.cruddemo.security.entity.user.User;
import com.luv2code.springboot.cruddemo.security.service.TokenService;
import com.luv2code.springboot.cruddemo.security.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;
    @Autowired
    AuthenticationManager authenticationManager;
    public class AuthenticationResponse {
        private String message;
        private String token;

        public AuthenticationResponse(String message, String token) {
            this.message = message;
            this.token = token;
        }

        // Getters e setters
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody @Valid RegisterDTO data) {
        User user = userService.findUserByEmail(data.email());
        if (user != null) {
            return ResponseEntity.badRequest().body("O usuario com esse email ja existe");
        } else {
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            User newUser = userService.createUser(new User(data.email(), encryptedPassword, data.role()));
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO data){
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());




            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Falha na autenticação: " + e.getMessage());
        }
    }

}

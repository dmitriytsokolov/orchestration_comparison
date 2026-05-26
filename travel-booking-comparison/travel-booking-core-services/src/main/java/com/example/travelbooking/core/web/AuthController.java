package com.example.travelbooking.core.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelbooking.core.service.UserService;
import com.example.travelbooking.domain.dto.AuthDtos.LoginRequest;
import com.example.travelbooking.domain.dto.AuthDtos.LoginResponse;
import com.example.travelbooking.domain.dto.AuthDtos.RegisterRequest;
import com.example.travelbooking.domain.entity.User;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final UserService userService;

  public AuthController(final UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  User register(@RequestBody final RegisterRequest request) {
    return userService.register(request);
  }

  @PostMapping("/login")
  LoginResponse login(@RequestBody final LoginRequest request) {
    return userService.login(request);
  }
}

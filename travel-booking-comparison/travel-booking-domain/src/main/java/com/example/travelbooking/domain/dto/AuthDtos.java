package com.example.travelbooking.domain.dto;

public final class AuthDtos {
  private AuthDtos() {
  }

  public record RegisterRequest(
      String email,
      String password,
      String firstName,
      String lastName) {
  }

  public record LoginRequest(String email, String password) {
  }

  public record LoginResponse(String token) {
  }
}

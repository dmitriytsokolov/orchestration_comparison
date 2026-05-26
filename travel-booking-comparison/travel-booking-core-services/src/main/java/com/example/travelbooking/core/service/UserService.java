package com.example.travelbooking.core.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.travelbooking.core.exception.UserNotFoundException;
import com.example.travelbooking.core.repository.UserRepository;
import com.example.travelbooking.domain.dto.AuthDtos.LoginRequest;
import com.example.travelbooking.domain.dto.AuthDtos.LoginResponse;
import com.example.travelbooking.domain.dto.AuthDtos.RegisterRequest;
import com.example.travelbooking.domain.entity.User;

@Service
public class UserService {
  private static final String HMAC_ALGORITHM = "HmacSHA256";
  private static final String JWT_SECRET = "local-dev-secret-change-me";

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      final UserRepository userRepository,
      final PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Transactional
  public User register(final RegisterRequest request) {
    final var user = new User(
        UUID.randomUUID(),
        request.email(),
        passwordEncoder.encode(request.password()),
        request.firstName(),
        request.lastName(),
        Instant.now());
    return userRepository.save(user);
  }

  @Transactional(readOnly = true)
  public LoginResponse login(final LoginRequest request) {
    final var user = userRepository.findByEmail(request.email())
        .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }
    return new LoginResponse(createToken(user));
  }

  @Transactional(readOnly = true)
  public User findById(final UUID userId) {
    return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
  }

  private String createToken(final User user) {
    final var header = encodeJson("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
    final var payload = encodeJson("{\"sub\":\"" + user.getId() + "\",\"email\":\""
        + user.getEmail() + "\",\"iat\":" + Instant.now().getEpochSecond() + "}");
    final var signingInput = header + "." + payload;
    return signingInput + "." + sign(signingInput);
  }

  private String encodeJson(final String json) {
    return Base64.getUrlEncoder()
        .withoutPadding()
        .encodeToString(json.getBytes(StandardCharsets.UTF_8));
  }

  private String sign(final String signingInput) {
    try {
      final var mac = Mac.getInstance(HMAC_ALGORITHM);
      mac.init(new SecretKeySpec(JWT_SECRET.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
      return Base64.getUrlEncoder().withoutPadding()
          .encodeToString(mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8)));
    } catch (final Exception ex) {
      throw new IllegalStateException("Failed to sign JWT: " + ex.getMessage(), ex);
    }
  }
}

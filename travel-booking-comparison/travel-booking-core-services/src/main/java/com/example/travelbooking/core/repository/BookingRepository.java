package com.example.travelbooking.core.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelbooking.domain.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
  Optional<Booking> findByIdempotencyKey(String idempotencyKey);

  List<Booking> findByUserIdOrderByCreatedAtDesc(UUID userId);
}

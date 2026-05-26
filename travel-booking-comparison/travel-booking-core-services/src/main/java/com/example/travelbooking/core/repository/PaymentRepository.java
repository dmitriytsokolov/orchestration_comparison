package com.example.travelbooking.core.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelbooking.domain.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
  Optional<Payment> findByBookingId(UUID bookingId);

  Optional<Payment> findByIdempotencyKey(String idempotencyKey);
}

package com.example.travelbooking.core.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.travelbooking.core.repository.BookingRepository;
import com.example.travelbooking.domain.entity.Booking;

@Service
public class IdempotencyService {
  private final BookingRepository bookingRepository;

  public IdempotencyService(final BookingRepository bookingRepository) {
    this.bookingRepository = bookingRepository;
  }

  @Transactional(readOnly = true)
  public Optional<Booking> findBooking(final String idempotencyKey) {
    return bookingRepository.findByIdempotencyKey(idempotencyKey);
  }
}

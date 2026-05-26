package com.example.travelbooking.core.exception;

import java.util.UUID;

public class BookingNotFoundException extends RuntimeException {
  public BookingNotFoundException(final UUID bookingId) {
    super("Booking not found: " + bookingId);
  }
}

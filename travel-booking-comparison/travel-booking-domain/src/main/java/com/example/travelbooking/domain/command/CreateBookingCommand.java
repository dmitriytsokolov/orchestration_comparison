package com.example.travelbooking.domain.command;

import java.util.UUID;

public record CreateBookingCommand(
    UUID bookingId,
    UUID userId,
    UUID flightId,
    UUID hotelId,
    UUID carRentalId,
    String idempotencyKey) {
}

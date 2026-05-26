package com.example.travelbooking.domain.dto;

import java.util.UUID;

public record CreateBookingRequest(
    UUID flightId,
    UUID hotelId,
    UUID carRentalId,
    UUID userId,
    String idempotencyKey) {
}

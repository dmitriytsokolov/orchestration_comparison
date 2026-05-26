package com.example.travelbooking.domain.event;

import java.util.UUID;

public record BookingCreatedEvent(
    UUID bookingId,
    UUID userId,
    UUID flightId,
    UUID hotelId,
    UUID carRentalId) {
}

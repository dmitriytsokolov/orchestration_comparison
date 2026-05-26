package com.example.travelbooking.domain.event;

import java.util.UUID;

public record FlightReservationFailedEvent(UUID flightId, UUID bookingId, String reason) {
}

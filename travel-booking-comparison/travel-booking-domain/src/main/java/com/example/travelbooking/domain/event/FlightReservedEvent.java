package com.example.travelbooking.domain.event;

import java.util.UUID;

public record FlightReservedEvent(UUID flightId, UUID bookingId) {
}

package com.example.travelbooking.domain.event;

import java.util.UUID;

public record CarReservationFailedEvent(UUID carRentalId, UUID bookingId, String reason) {
}

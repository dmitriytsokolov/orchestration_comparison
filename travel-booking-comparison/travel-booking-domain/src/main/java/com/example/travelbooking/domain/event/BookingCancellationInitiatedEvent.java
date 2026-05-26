package com.example.travelbooking.domain.event;

import java.util.UUID;

public record BookingCancellationInitiatedEvent(UUID bookingId, String reason) {
}

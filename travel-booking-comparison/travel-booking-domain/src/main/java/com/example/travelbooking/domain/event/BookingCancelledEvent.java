package com.example.travelbooking.domain.event;

import java.util.UUID;

public record BookingCancelledEvent(UUID bookingId, String reason) {
}

package com.example.travelbooking.domain.event;

import java.util.UUID;

public record PaymentFailedEvent(UUID bookingId, String reason) {
}

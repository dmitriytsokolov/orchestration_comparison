package com.example.travelbooking.domain.event;

import java.util.UUID;

public record BookingConfirmedEvent(UUID bookingId) {
}

package com.example.travelbooking.domain.event;

import java.util.UUID;

public record CarReservedEvent(UUID carRentalId, UUID bookingId) {
}

package com.example.travelbooking.domain.command;

import java.util.UUID;

public record ReserveFlightCommand(UUID flightId, UUID bookingId) {
}

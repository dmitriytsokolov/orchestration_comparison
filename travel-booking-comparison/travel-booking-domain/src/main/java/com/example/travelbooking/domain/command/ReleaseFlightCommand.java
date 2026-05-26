package com.example.travelbooking.domain.command;

import java.util.UUID;

public record ReleaseFlightCommand(UUID flightId, UUID bookingId) {
}

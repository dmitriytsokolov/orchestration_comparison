package com.example.travelbooking.domain.command;

import java.util.UUID;

public record CancelBookingCommand(UUID bookingId, String reason) {
}

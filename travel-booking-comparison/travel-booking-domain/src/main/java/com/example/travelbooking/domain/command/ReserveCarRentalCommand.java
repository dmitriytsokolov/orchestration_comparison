package com.example.travelbooking.domain.command;

import java.util.UUID;

public record ReserveCarRentalCommand(UUID carRentalId, UUID bookingId) {
}

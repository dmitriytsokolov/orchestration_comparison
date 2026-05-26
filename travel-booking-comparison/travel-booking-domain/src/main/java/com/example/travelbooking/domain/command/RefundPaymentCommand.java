package com.example.travelbooking.domain.command;

import java.util.UUID;

public record RefundPaymentCommand(UUID bookingId, String gatewayRef, String idempotencyKey) {
}

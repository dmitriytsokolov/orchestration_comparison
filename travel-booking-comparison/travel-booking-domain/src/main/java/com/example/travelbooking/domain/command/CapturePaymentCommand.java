package com.example.travelbooking.domain.command;

import java.math.BigDecimal;
import java.util.UUID;

public record CapturePaymentCommand(
    UUID bookingId,
    BigDecimal amount,
    String currency,
    String idempotencyKey) {
}

package com.example.travelbooking.domain.event;

import java.util.UUID;

public record PaymentCapturedEvent(UUID bookingId, String gatewayRef) {
}

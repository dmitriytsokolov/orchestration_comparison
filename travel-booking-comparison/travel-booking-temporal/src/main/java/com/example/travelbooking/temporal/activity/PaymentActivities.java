package com.example.travelbooking.temporal.activity;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.travelbooking.domain.dto.PaymentResult;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface PaymentActivities {
  PaymentResult capturePayment(UUID bookingId, BigDecimal amount, String currency, String idempotencyKey);

  void refundPayment(UUID bookingId, String idempotencyKey);
}

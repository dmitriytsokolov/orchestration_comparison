package com.example.travelbooking.temporal.activity;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.PaymentService;
import com.example.travelbooking.domain.dto.PaymentResult;

@Component
public class PaymentActivitiesImpl implements PaymentActivities {
  private final PaymentService paymentService;

  public PaymentActivitiesImpl(final PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @Override
  public PaymentResult capturePayment(
      final UUID bookingId,
      final BigDecimal amount,
      final String currency,
      final String idempotencyKey) {
    return paymentService.capture(bookingId, amount, currency, idempotencyKey);
  }

  @Override
  public void refundPayment(final UUID bookingId, final String idempotencyKey) {
    paymentService.refund(bookingId, idempotencyKey);
  }
}

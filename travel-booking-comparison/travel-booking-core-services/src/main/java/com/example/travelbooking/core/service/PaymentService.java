package com.example.travelbooking.core.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.travelbooking.core.exception.PaymentFailedException;
import com.example.travelbooking.core.repository.PaymentRepository;
import com.example.travelbooking.domain.dto.PaymentResult;
import com.example.travelbooking.domain.dto.RefundResult;
import com.example.travelbooking.domain.entity.Payment;
import com.example.travelbooking.domain.entity.PaymentStatus;

@Service
public class PaymentService {
  private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

  private final PaymentGatewayClient paymentGatewayClient;
  private final PaymentRepository paymentRepository;

  public PaymentService(
      final PaymentGatewayClient paymentGatewayClient,
      final PaymentRepository paymentRepository) {
    this.paymentGatewayClient = paymentGatewayClient;
    this.paymentRepository = paymentRepository;
  }

  @Transactional
  public PaymentResult capture(
      final UUID bookingId,
      final BigDecimal amount,
      final String currency,
      final String idempotencyKey) {
    final var existing = paymentRepository.findByIdempotencyKey(idempotencyKey);
    if (existing.isPresent() && existing.get().getStatus() == PaymentStatus.CAPTURED) {
      return new PaymentResult(true, existing.get().getGatewayRef(), null);
    }
    final var payment = existing.orElseGet(() -> paymentRepository.save(
        new Payment(UUID.randomUUID(), bookingId, amount, currency, idempotencyKey)));
    final var result = paymentGatewayClient.capture(amount, currency, idempotencyKey);
    if (result == null || !result.successful()) {
      payment.fail();
      throw new PaymentFailedException(result == null ? "Payment gateway returned no body" : result.failureReason());
    }
    payment.capture(result.gatewayRef());
    log.info("Captured payment bookingId={} gatewayRef={}", bookingId, result.gatewayRef());
    return result;
  }

  @Transactional
  public RefundResult refund(final UUID bookingId, final String idempotencyKey) {
    final var payment = paymentRepository.findByBookingId(bookingId)
        .orElseThrow(() -> new PaymentFailedException("Payment not found for booking: " + bookingId));
    if (payment.getStatus() == PaymentStatus.REFUNDED) {
      return new RefundResult(true, payment.getGatewayRef());
    }
    final var result = paymentGatewayClient.refund(payment.getGatewayRef(), idempotencyKey);
    if (result == null || !result.successful()) {
      throw new PaymentFailedException("Refund failed for booking: " + bookingId);
    }
    payment.refund();
    log.info("Refunded payment bookingId={} gatewayRef={}", bookingId, payment.getGatewayRef());
    return result;
  }
}

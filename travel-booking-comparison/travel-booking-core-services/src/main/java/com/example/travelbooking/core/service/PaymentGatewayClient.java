package com.example.travelbooking.core.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.travelbooking.domain.dto.PaymentResult;
import com.example.travelbooking.domain.dto.RefundResult;

@Component
public class PaymentGatewayClient {
  private final RestClient paymentGatewayRestClient;

  public PaymentGatewayClient(final RestClient paymentGatewayRestClient) {
    this.paymentGatewayRestClient = paymentGatewayRestClient;
  }

  public PaymentResult capture(
      final BigDecimal amount,
      final String currency,
      final String idempotencyKey) {
    return paymentGatewayRestClient.post()
        .uri("/payments/capture")
        .body(new CaptureRequest(amount, currency, idempotencyKey))
        .retrieve()
        .body(PaymentResult.class);
  }

  public RefundResult refund(final String gatewayRef, final String idempotencyKey) {
    return paymentGatewayRestClient.post()
        .uri("/payments/refund")
        .body(new RefundRequest(gatewayRef, idempotencyKey))
        .retrieve()
        .body(RefundResult.class);
  }

  private record CaptureRequest(BigDecimal amount, String currency, String idempotencyKey) {
  }

  private record RefundRequest(String gatewayRef, String idempotencyKey) {
  }
}

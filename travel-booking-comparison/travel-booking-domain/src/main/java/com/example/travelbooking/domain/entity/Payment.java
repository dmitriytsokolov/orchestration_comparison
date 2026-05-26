package com.example.travelbooking.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {
  @Id
  private UUID id;
  @Column(nullable = false)
  private UUID bookingId;
  @Column(nullable = false)
  private BigDecimal amount;
  @Column(nullable = false)
  private String currency;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;
  @Column(nullable = false, unique = true)
  private String idempotencyKey;
  private String gatewayRef;

  protected Payment() {
  }

  public Payment(
      final UUID id,
      final UUID bookingId,
      final BigDecimal amount,
      final String currency,
      final String idempotencyKey) {
    this.id = id;
    this.bookingId = bookingId;
    this.amount = amount;
    this.currency = currency;
    this.status = PaymentStatus.PENDING;
    this.idempotencyKey = idempotencyKey;
  }

  public UUID getId() {
    return id;
  }

  public UUID getBookingId() {
    return bookingId;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public String getCurrency() {
    return currency;
  }

  public PaymentStatus getStatus() {
    return status;
  }

  public String getIdempotencyKey() {
    return idempotencyKey;
  }

  public String getGatewayRef() {
    return gatewayRef;
  }

  public void capture(final String gatewayRef) {
    this.gatewayRef = gatewayRef;
    status = PaymentStatus.CAPTURED;
  }

  public void refund() {
    status = PaymentStatus.REFUNDED;
  }

  public void fail() {
    status = PaymentStatus.FAILED;
  }
}

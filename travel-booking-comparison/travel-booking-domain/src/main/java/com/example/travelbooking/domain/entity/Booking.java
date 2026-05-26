package com.example.travelbooking.domain.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bookings")
public class Booking {
  @Id
  private UUID id;
  @Column(nullable = false)
  private UUID userId;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BookingStatus status;
  @Column(nullable = false)
  private UUID flightId;
  @Column(nullable = false)
  private UUID hotelId;
  @Column(nullable = false)
  private UUID carRentalId;
  @Column(nullable = false)
  private BigDecimal totalAmount;
  @Column(nullable = false, unique = true)
  private String idempotencyKey;
  private String orchestrationId;
  @Column(nullable = false)
  private Instant createdAt;
  @Column(nullable = false)
  private Instant updatedAt;

  protected Booking() {
  }

  public Booking(
      final UUID id,
      final UUID userId,
      final UUID flightId,
      final UUID hotelId,
      final UUID carRentalId,
      final BigDecimal totalAmount,
      final String idempotencyKey,
      final Instant now) {
    this.id = id;
    this.userId = userId;
    this.status = BookingStatus.PENDING;
    this.flightId = flightId;
    this.hotelId = hotelId;
    this.carRentalId = carRentalId;
    this.totalAmount = totalAmount;
    this.idempotencyKey = idempotencyKey;
    this.createdAt = now;
    this.updatedAt = now;
  }

  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public BookingStatus getStatus() {
    return status;
  }

  public UUID getFlightId() {
    return flightId;
  }

  public UUID getHotelId() {
    return hotelId;
  }

  public UUID getCarRentalId() {
    return carRentalId;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public String getIdempotencyKey() {
    return idempotencyKey;
  }

  public String getOrchestrationId() {
    return orchestrationId;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setOrchestrationId(final String orchestrationId) {
    this.orchestrationId = orchestrationId;
    touch();
  }

  public void confirm() {
    status = BookingStatus.CONFIRMED;
    touch();
  }

  public void cancel() {
    status = BookingStatus.CANCELLED;
    touch();
  }

  public void markModificationPending() {
    status = BookingStatus.MODIFICATION_PENDING;
    touch();
  }

  public void modify(final UUID newFlightId, final UUID newHotelId, final UUID newCarRentalId) {
    flightId = newFlightId;
    hotelId = newHotelId;
    carRentalId = newCarRentalId;
    status = BookingStatus.CONFIRMED;
    touch();
  }

  private void touch() {
    updatedAt = Instant.now();
  }
}

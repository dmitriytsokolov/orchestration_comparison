package com.example.travelbooking.domain.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking_status_history")
public class BookingStatusHistory {
  @Id
  @GeneratedValue
  private UUID id;
  @Column(nullable = false)
  private UUID bookingId;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BookingStatus status;
  @Column(nullable = false)
  private Instant timestamp;
  @Column(nullable = false)
  private String reason;

  protected BookingStatusHistory() {
  }

  public BookingStatusHistory(
      final UUID bookingId,
      final BookingStatus status,
      final Instant timestamp,
      final String reason) {
    this.bookingId = bookingId;
    this.status = status;
    this.timestamp = timestamp;
    this.reason = reason;
  }

  public UUID getBookingId() {
    return bookingId;
  }

  public BookingStatus getStatus() {
    return status;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public String getReason() {
    return reason;
  }
}

package com.example.travelbooking.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "flights")
public class Flight {
  @Id
  private UUID id;
  @Column(nullable = false)
  private String flightNumber;
  @Column(nullable = false)
  private String origin;
  @Column(nullable = false)
  private String destination;
  @Column(nullable = false)
  private LocalDateTime departureTime;
  @Column(nullable = false)
  private LocalDateTime arrivalTime;
  @Column(nullable = false)
  private int availableSeats;
  @Column(nullable = false)
  private BigDecimal pricePerSeat;
  @Column(nullable = false)
  private String currency;

  protected Flight() {
  }

  public Flight(
      final UUID id,
      final String flightNumber,
      final String origin,
      final String destination,
      final LocalDateTime departureTime,
      final LocalDateTime arrivalTime,
      final int availableSeats,
      final BigDecimal pricePerSeat,
      final String currency) {
    this.id = id;
    this.flightNumber = flightNumber;
    this.origin = origin;
    this.destination = destination;
    this.departureTime = departureTime;
    this.arrivalTime = arrivalTime;
    this.availableSeats = availableSeats;
    this.pricePerSeat = pricePerSeat;
    this.currency = currency;
  }

  public UUID getId() {
    return id;
  }

  public String getFlightNumber() {
    return flightNumber;
  }

  public String getOrigin() {
    return origin;
  }

  public String getDestination() {
    return destination;
  }

  public LocalDateTime getDepartureTime() {
    return departureTime;
  }

  public LocalDateTime getArrivalTime() {
    return arrivalTime;
  }

  public int getAvailableSeats() {
    return availableSeats;
  }

  public void reserveSeat() {
    availableSeats--;
  }

  public void releaseSeat() {
    availableSeats++;
  }

  public BigDecimal getPricePerSeat() {
    return pricePerSeat;
  }

  public String getCurrency() {
    return currency;
  }
}

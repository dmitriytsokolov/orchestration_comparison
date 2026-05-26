package com.example.travelbooking.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hotels")
public class Hotel {
  @Id
  private UUID id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String city;
  @Column(nullable = false)
  private String address;
  @Column(nullable = false)
  private LocalDate checkInDate;
  @Column(nullable = false)
  private LocalDate checkOutDate;
  @Column(nullable = false)
  private int availableRooms;
  @Column(nullable = false)
  private BigDecimal pricePerNight;
  @Column(nullable = false)
  private String currency;

  protected Hotel() {
  }

  public Hotel(
      final UUID id,
      final String name,
      final String city,
      final String address,
      final LocalDate checkInDate,
      final LocalDate checkOutDate,
      final int availableRooms,
      final BigDecimal pricePerNight,
      final String currency) {
    this.id = id;
    this.name = name;
    this.city = city;
    this.address = address;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.availableRooms = availableRooms;
    this.pricePerNight = pricePerNight;
    this.currency = currency;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCity() {
    return city;
  }

  public String getAddress() {
    return address;
  }

  public LocalDate getCheckInDate() {
    return checkInDate;
  }

  public LocalDate getCheckOutDate() {
    return checkOutDate;
  }

  public int getAvailableRooms() {
    return availableRooms;
  }

  public void reserveRoom() {
    availableRooms--;
  }

  public void releaseRoom() {
    availableRooms++;
  }

  public BigDecimal getPricePerNight() {
    return pricePerNight;
  }

  public String getCurrency() {
    return currency;
  }
}

package com.example.travelbooking.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "car_rentals")
public class CarRental {
  @Id
  private UUID id;
  @Column(nullable = false)
  private String company;
  @Column(nullable = false)
  private String model;
  @Column(nullable = false)
  private String pickupLocation;
  @Column(nullable = false)
  private String dropoffLocation;
  @Column(nullable = false)
  private LocalDate pickupDate;
  @Column(nullable = false)
  private LocalDate dropoffDate;
  @Column(nullable = false)
  private boolean available;
  @Column(nullable = false)
  private BigDecimal pricePerDay;
  @Column(nullable = false)
  private String currency;

  protected CarRental() {
  }

  public CarRental(
      final UUID id,
      final String company,
      final String model,
      final String pickupLocation,
      final String dropoffLocation,
      final LocalDate pickupDate,
      final LocalDate dropoffDate,
      final boolean available,
      final BigDecimal pricePerDay,
      final String currency) {
    this.id = id;
    this.company = company;
    this.model = model;
    this.pickupLocation = pickupLocation;
    this.dropoffLocation = dropoffLocation;
    this.pickupDate = pickupDate;
    this.dropoffDate = dropoffDate;
    this.available = available;
    this.pricePerDay = pricePerDay;
    this.currency = currency;
  }

  public UUID getId() {
    return id;
  }

  public String getCompany() {
    return company;
  }

  public String getModel() {
    return model;
  }

  public String getPickupLocation() {
    return pickupLocation;
  }

  public String getDropoffLocation() {
    return dropoffLocation;
  }

  public LocalDate getPickupDate() {
    return pickupDate;
  }

  public LocalDate getDropoffDate() {
    return dropoffDate;
  }

  public boolean isAvailable() {
    return available;
  }

  public void reserve() {
    available = false;
  }

  public void release() {
    available = true;
  }

  public BigDecimal getPricePerDay() {
    return pricePerDay;
  }

  public String getCurrency() {
    return currency;
  }
}

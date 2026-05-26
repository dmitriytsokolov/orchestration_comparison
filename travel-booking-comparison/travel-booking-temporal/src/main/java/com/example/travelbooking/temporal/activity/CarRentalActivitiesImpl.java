package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.InventoryService;

@Component
public class CarRentalActivitiesImpl implements CarRentalActivities {
  private final InventoryService inventoryService;

  public CarRentalActivitiesImpl(final InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @Override
  public void reserveCarRental(final UUID carRentalId, final UUID bookingId) {
    inventoryService.reserveCarRental(carRentalId);
  }

  @Override
  public void releaseCarRental(final UUID carRentalId, final UUID bookingId) {
    inventoryService.releaseCarRental(carRentalId);
  }
}

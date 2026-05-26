package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.InventoryService;

@Component
public class FlightActivitiesImpl implements FlightActivities {
  private final InventoryService inventoryService;

  public FlightActivitiesImpl(final InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @Override
  public void reserveFlight(final UUID flightId, final UUID bookingId) {
    inventoryService.reserveFlight(flightId);
  }

  @Override
  public void releaseFlight(final UUID flightId, final UUID bookingId) {
    inventoryService.releaseFlight(flightId);
  }
}

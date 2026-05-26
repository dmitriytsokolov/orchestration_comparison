package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.InventoryService;

@Component
public class HotelActivitiesImpl implements HotelActivities {
  private final InventoryService inventoryService;

  public HotelActivitiesImpl(final InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @Override
  public void reserveHotel(final UUID hotelId, final UUID bookingId) {
    inventoryService.reserveHotel(hotelId);
  }

  @Override
  public void releaseHotel(final UUID hotelId, final UUID bookingId) {
    inventoryService.releaseHotel(hotelId);
  }
}

package com.example.travelbooking.temporal.activity;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.BookingService;
import com.example.travelbooking.core.service.InventoryService;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;

@Component
public class BookingActivitiesImpl implements BookingActivities {
  private final BookingService bookingService;
  private final InventoryService inventoryService;

  public BookingActivitiesImpl(
      final BookingService bookingService,
      final InventoryService inventoryService) {
    this.bookingService = bookingService;
    this.inventoryService = inventoryService;
  }

  @Override
  public void confirmBooking(final UUID bookingId) {
    bookingService.confirm(bookingId);
  }

  @Override
  public void cancelBooking(final UUID bookingId, final String reason) {
    bookingService.cancel(bookingId, reason);
  }

  @Override
  public void modifyBooking(final UUID bookingId, final ModifyBookingRequest request) {
    bookingService.modify(bookingId, request);
  }

  @Override
  public void releaseReservations(final UUID bookingId) {
    final var booking = bookingService.findBooking(bookingId);
    inventoryService.releaseCarRental(booking.getCarRentalId());
    inventoryService.releaseHotel(booking.getHotelId());
    inventoryService.releaseFlight(booking.getFlightId());
  }
}

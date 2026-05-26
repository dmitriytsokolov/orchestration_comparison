package com.example.travelbooking.core.web;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelbooking.core.service.BookingOrchestrator;
import com.example.travelbooking.core.service.BookingService;
import com.example.travelbooking.domain.dto.BookingResponse;
import com.example.travelbooking.domain.dto.BookingStatusResponse;
import com.example.travelbooking.domain.dto.CreateBookingRequest;
import com.example.travelbooking.domain.dto.ModifyBookingRequest;

@RestController
@RequestMapping("/api/v1")
public class BookingController {
  private final BookingService bookingService;
  private final BookingOrchestrator bookingOrchestrator;

  public BookingController(
      final BookingService bookingService,
      final BookingOrchestrator bookingOrchestrator) {
    this.bookingService = bookingService;
    this.bookingOrchestrator = bookingOrchestrator;
  }

  @PostMapping("/bookings")
  BookingResponse createBooking(
      @RequestHeader("X-Idempotency-Key") final String idempotencyKey,
      @RequestBody final CreateBookingRequest request) {
    final var enrichedRequest = new CreateBookingRequest(
        request.flightId(),
        request.hotelId(),
        request.carRentalId(),
        request.userId(),
        idempotencyKey);
    final var booking = bookingService.createPending(enrichedRequest);
    if (booking.getOrchestrationId() == null) {
      final var orchestrationId = bookingOrchestrator.startBooking(booking.getId(), enrichedRequest);
      bookingService.attachOrchestrationId(booking.getId(), orchestrationId);
    }
    return bookingService.getBooking(booking.getId());
  }

  @GetMapping("/bookings/{id}")
  BookingStatusResponse getBooking(@PathVariable final UUID id) {
    return bookingService.getStatus(id);
  }

  @DeleteMapping("/bookings/{id}")
  BookingStatusResponse cancelBooking(@PathVariable final UUID id) {
    final var orchestrationId = bookingOrchestrator.startCancellation(id);
    bookingService.attachOrchestrationId(id, orchestrationId);
    return bookingService.getStatus(id);
  }

  @PutMapping("/bookings/{id}")
  BookingStatusResponse modifyBooking(
      @PathVariable final UUID id,
      @RequestBody final ModifyBookingRequest request) {
    final var orchestrationId = bookingOrchestrator.startModification(id, request);
    bookingService.attachOrchestrationId(id, orchestrationId);
    return bookingService.getStatus(id);
  }

  @GetMapping("/users/{id}/bookings")
  List<BookingResponse> getUserBookings(@PathVariable final UUID id) {
    return bookingService.getUserBookings(id);
  }
}

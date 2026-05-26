package com.example.travelbooking.axon.query;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.example.travelbooking.core.service.BookingService;
import com.example.travelbooking.domain.dto.BookingResponse;

@Component
public class BookingQueryHandler {
  private final BookingService bookingService;

  public BookingQueryHandler(final BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @QueryHandler
  public BookingResponse handle(final GetBookingQuery query) {
    return bookingService.getBooking(query.bookingId());
  }

  @QueryHandler
  public List<BookingResponse> handle(final GetUserBookingsQuery query) {
    return bookingService.getUserBookings(query.userId());
  }
}

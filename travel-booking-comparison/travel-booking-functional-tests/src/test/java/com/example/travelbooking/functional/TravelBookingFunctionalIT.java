package com.example.travelbooking.functional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.example.travelbooking.domain.entity.BookingStatus;

class TravelBookingFunctionalIT extends AbstractFunctionalTest {
  @ParameterizedTest
  @MethodSource("implementations")
  void shouldCompleteHappyPath(final Implementation implementation) {
    useImplementation(implementation);
    final var bookingId = bookTrip("happy-" + UUID.randomUUID(), 1);
    awaitBookingStatus(bookingId, BookingStatus.CONFIRMED);
  }

  @ParameterizedTest
  @MethodSource("implementations")
  void shouldCompensatePaymentFailure(final Implementation implementation) {
    useImplementation(implementation);
    final var bookingId = bookTrip("fail-" + UUID.randomUUID(), 1);
    awaitBookingStatus(bookingId, BookingStatus.CANCELLED);
  }

  @ParameterizedTest
  @MethodSource("implementations")
  void shouldCompensateHotelUnavailability(final Implementation implementation) {
    useImplementation(implementation);
    final var bookingId = bookTrip("hotel-unavailable-" + UUID.randomUUID(), 0);
    awaitBookingStatus(bookingId, BookingStatus.CANCELLED);
  }

  @ParameterizedTest
  @MethodSource("implementations")
  void shouldCancelWithRefund(final Implementation implementation) {
    useImplementation(implementation);
    final var bookingId = bookTrip("cancel-" + UUID.randomUUID(), 1);
    awaitBookingStatus(bookingId, BookingStatus.CONFIRMED);
    io.restassured.RestAssured.delete("/api/v1/bookings/" + bookingId)
        .then()
        .statusCode(200);
    awaitBookingStatus(bookingId, BookingStatus.CANCELLED);
  }

  @ParameterizedTest
  @MethodSource("implementations")
  void shouldReturnSameBookingForSameIdempotencyKey(final Implementation implementation) {
    useImplementation(implementation);
    final var idempotencyKey = "idem-" + UUID.randomUUID();
    final var userId = registerUser();
    final var request = buildBookingRequest(
        userId,
        seedFlight(2),
        seedHotel(2),
        seedCarRental(),
        idempotencyKey);
    final var firstBookingId = createBooking(request);
    final var secondBookingId = createBooking(request);
    assertThat(secondBookingId).isEqualTo(firstBookingId);
  }

  @ParameterizedTest
  @MethodSource("implementations")
  void shouldRetryPaymentTimeout(final Implementation implementation) {
    useImplementation(implementation);
    final var bookingId = bookTrip("timeout-" + UUID.randomUUID(), 1);
    awaitBookingStatus(bookingId, BookingStatus.CONFIRMED);
  }

  @ParameterizedTest
  @MethodSource("implementations")
  void shouldModifyBooking(final Implementation implementation) {
    useImplementation(implementation);
    final var bookingId = bookTrip("modify-" + UUID.randomUUID(), 1);
    awaitBookingStatus(bookingId, BookingStatus.CONFIRMED);
  }

  private String bookTrip(final String idempotencyKey, final int hotelRooms) {
    final var request = buildBookingRequest(
        registerUser(),
        seedFlight(1),
        seedHotel(hotelRooms),
        seedCarRental(),
        idempotencyKey);
    return createBooking(request);
  }

  private static Stream<Arguments> implementations() {
    return Stream.of(Implementation.values()).map(Arguments::of);
  }
}

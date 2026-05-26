package com.example.travelbooking.functional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.time.Duration;
import java.util.UUID;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;

import com.example.travelbooking.domain.dto.CreateBookingRequest;
import com.example.travelbooking.domain.entity.BookingStatus;

import io.restassured.RestAssured;

public abstract class AbstractFunctionalTest {
  protected Implementation implementation;

  @BeforeEach
  void resetRestAssured() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  protected void useImplementation(final Implementation implementation) {
    this.implementation = implementation;
    RestAssured.baseURI = implementation.baseUrl();
  }

  protected UUID seedFlight(final int seats) {
    return UUID.fromString(given()
        .contentType("application/json")
        .body("""
            {
              "id": "%s",
              "flightNumber": "TB100",
              "origin": "BUD",
              "destination": "LHR",
              "departureTime": "2026-06-01T10:00:00",
              "arrivalTime": "2026-06-01T12:00:00",
              "availableSeats": %d,
              "pricePerSeat": 120.00,
              "currency": "USD"
            }
            """.formatted(UUID.randomUUID(), seats))
        .post("/api/v1/admin/flights")
        .then()
        .statusCode(200)
        .extract()
        .path("id"));
  }

  protected UUID seedHotel(final int rooms) {
    return UUID.fromString(given()
        .contentType("application/json")
        .body("""
            {
              "id": "%s",
              "name": "Central Hotel",
              "city": "London",
              "address": "1 Main Street",
              "checkInDate": "2026-06-01",
              "checkOutDate": "2026-06-04",
              "availableRooms": %d,
              "pricePerNight": 80.00,
              "currency": "USD"
            }
            """.formatted(UUID.randomUUID(), rooms))
        .post("/api/v1/admin/hotels")
        .then()
        .statusCode(200)
        .extract()
        .path("id"));
  }

  protected UUID seedCarRental() {
    return UUID.fromString(given()
        .contentType("application/json")
        .body("""
            {
              "id": "%s",
              "company": "RoadRunner",
              "model": "Compact",
              "pickupLocation": "LHR",
              "dropoffLocation": "LHR",
              "pickupDate": "2026-06-01",
              "dropoffDate": "2026-06-04",
              "available": true,
              "pricePerDay": 40.00,
              "currency": "USD"
            }
            """.formatted(UUID.randomUUID()))
        .post("/api/v1/admin/car-rentals")
        .then()
        .statusCode(200)
        .extract()
        .path("id"));
  }

  protected UUID registerUser() {
    return UUID.fromString(given()
        .contentType("application/json")
        .body("""
            {
              "email": "user-%s@example.com",
              "password": "secret",
              "firstName": "Test",
              "lastName": "User"
            }
            """.formatted(UUID.randomUUID()))
        .post("/api/v1/auth/register")
        .then()
        .statusCode(200)
        .extract()
        .path("id"));
  }

  protected CreateBookingRequest buildBookingRequest(
      final UUID userId,
      final UUID flightId,
      final UUID hotelId,
      final UUID carRentalId,
      final String idempotencyKey) {
    return new CreateBookingRequest(flightId, hotelId, carRentalId, userId, idempotencyKey);
  }

  protected String createBooking(final CreateBookingRequest request) {
    return given()
        .contentType("application/json")
        .header("X-Idempotency-Key", request.idempotencyKey())
        .body(request)
        .post("/api/v1/bookings")
        .then()
        .statusCode(200)
        .extract()
        .path("id");
  }

  protected void awaitBookingStatus(final String bookingId, final BookingStatus expected) {
    Awaitility.await()
        .atMost(Duration.ofSeconds(30))
        .pollInterval(Duration.ofMillis(500))
        .untilAsserted(() -> given()
            .get("/api/v1/bookings/" + bookingId)
            .then()
            .statusCode(200)
            .body("status", equalTo(expected.name())));
  }
}

package com.example.travelbooking.core.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.travelbooking.core.exception.InventoryUnavailableException;
import com.example.travelbooking.core.repository.CarRentalRepository;
import com.example.travelbooking.core.repository.FlightRepository;
import com.example.travelbooking.core.repository.HotelRepository;
import com.example.travelbooking.domain.dto.CarRentalDto;
import com.example.travelbooking.domain.dto.FlightDto;
import com.example.travelbooking.domain.dto.HotelDto;
import com.example.travelbooking.domain.dto.SearchCarRentalsRequest;
import com.example.travelbooking.domain.dto.SearchFlightsRequest;
import com.example.travelbooking.domain.dto.SearchHotelsRequest;
import com.example.travelbooking.domain.entity.CarRental;
import com.example.travelbooking.domain.entity.Flight;
import com.example.travelbooking.domain.entity.Hotel;

@Service
public class InventoryService {
  private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

  private final FlightRepository flightRepository;
  private final HotelRepository hotelRepository;
  private final CarRentalRepository carRentalRepository;
  private final TravelBookingMapper mapper;

  public InventoryService(
      final FlightRepository flightRepository,
      final HotelRepository hotelRepository,
      final CarRentalRepository carRentalRepository,
      final TravelBookingMapper mapper) {
    this.flightRepository = flightRepository;
    this.hotelRepository = hotelRepository;
    this.carRentalRepository = carRentalRepository;
    this.mapper = mapper;
  }

  @Transactional(readOnly = true)
  public List<FlightDto> searchFlights(final SearchFlightsRequest request) {
    final var start = request.date().atStartOfDay();
    final var end = start.plusDays(1);
    return flightRepository
        .findByOriginAndDestinationAndDepartureTimeBetweenAndAvailableSeatsGreaterThanEqual(
            request.origin(), request.destination(), start, end, request.passengers())
        .stream()
        .map(mapper::toDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<HotelDto> searchHotels(final SearchHotelsRequest request) {
    return hotelRepository
        .findByCityAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqualAndAvailableRoomsGreaterThanEqual(
            request.city(), request.checkIn(), request.checkOut(), request.rooms())
        .stream()
        .map(mapper::toDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<CarRentalDto> searchCarRentals(final SearchCarRentalsRequest request) {
    return carRentalRepository
        .findByPickupLocationAndPickupDateAndDropoffDateAndAvailableTrue(
            request.pickupLocation(), request.pickupDate(), request.dropoffDate())
        .stream()
        .map(mapper::toDto)
        .toList();
  }

  @Transactional
  public void reserveFlight(final UUID flightId) {
    final var flight = findFlight(flightId);
    if (flight.getAvailableSeats() < 1) {
      throw new InventoryUnavailableException("Flight has no available seats: " + flightId);
    }
    flight.reserveSeat();
    log.info("Reserved flight flightId={}", flightId);
  }

  @Transactional
  public void releaseFlight(final UUID flightId) {
    final var flight = findFlight(flightId);
    flight.releaseSeat();
    log.info("Released flight flightId={}", flightId);
  }

  @Transactional
  public void reserveHotel(final UUID hotelId) {
    final var hotel = findHotel(hotelId);
    if (hotel.getAvailableRooms() < 1) {
      throw new InventoryUnavailableException("Hotel has no available rooms: " + hotelId);
    }
    hotel.reserveRoom();
    log.info("Reserved hotel hotelId={}", hotelId);
  }

  @Transactional
  public void releaseHotel(final UUID hotelId) {
    final var hotel = findHotel(hotelId);
    hotel.releaseRoom();
    log.info("Released hotel hotelId={}", hotelId);
  }

  @Transactional
  public void reserveCarRental(final UUID carRentalId) {
    final var carRental = findCarRental(carRentalId);
    if (!carRental.isAvailable()) {
      throw new InventoryUnavailableException("Car rental is unavailable: " + carRentalId);
    }
    carRental.reserve();
    log.info("Reserved car rental carRentalId={}", carRentalId);
  }

  @Transactional
  public void releaseCarRental(final UUID carRentalId) {
    final var carRental = findCarRental(carRentalId);
    carRental.release();
    log.info("Released car rental carRentalId={}", carRentalId);
  }

  @Transactional
  public FlightDto seedFlight(final Flight flight) {
    return mapper.toDto(flightRepository.save(flight));
  }

  @Transactional
  public HotelDto seedHotel(final Hotel hotel) {
    return mapper.toDto(hotelRepository.save(hotel));
  }

  @Transactional
  public CarRentalDto seedCarRental(final CarRental carRental) {
    return mapper.toDto(carRentalRepository.save(carRental));
  }

  private Flight findFlight(final UUID flightId) {
    return flightRepository.findById(flightId)
        .orElseThrow(() -> new InventoryUnavailableException("Flight not found: " + flightId));
  }

  private Hotel findHotel(final UUID hotelId) {
    return hotelRepository.findById(hotelId)
        .orElseThrow(() -> new InventoryUnavailableException("Hotel not found: " + hotelId));
  }

  private CarRental findCarRental(final UUID carRentalId) {
    return carRentalRepository.findById(carRentalId)
        .orElseThrow(() -> new InventoryUnavailableException("Car rental not found: " + carRentalId));
  }
}

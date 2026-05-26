package com.example.travelbooking.core.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelbooking.domain.entity.Flight;

public interface FlightRepository extends JpaRepository<Flight, UUID> {
  List<Flight> findByOriginAndDestinationAndDepartureTimeBetweenAndAvailableSeatsGreaterThanEqual(
      String origin,
      String destination,
      LocalDateTime start,
      LocalDateTime end,
      int passengers);
}

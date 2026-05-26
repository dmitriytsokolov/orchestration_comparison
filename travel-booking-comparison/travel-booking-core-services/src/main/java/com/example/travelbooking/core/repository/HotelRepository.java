package com.example.travelbooking.core.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelbooking.domain.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
  List<Hotel> findByCityAndCheckInDateLessThanEqualAndCheckOutDateGreaterThanEqualAndAvailableRoomsGreaterThanEqual(
      String city,
      LocalDate checkIn,
      LocalDate checkOut,
      int rooms);
}

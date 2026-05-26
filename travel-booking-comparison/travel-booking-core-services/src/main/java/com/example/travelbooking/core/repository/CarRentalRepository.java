package com.example.travelbooking.core.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.travelbooking.domain.entity.CarRental;

public interface CarRentalRepository extends JpaRepository<CarRental, UUID> {
  List<CarRental> findByPickupLocationAndPickupDateAndDropoffDateAndAvailableTrue(
      String pickupLocation,
      LocalDate pickupDate,
      LocalDate dropoffDate);
}

package com.example.travelbooking.core.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelbooking.core.service.InventoryService;
import com.example.travelbooking.domain.dto.CarRentalDto;
import com.example.travelbooking.domain.dto.FlightDto;
import com.example.travelbooking.domain.dto.HotelDto;
import com.example.travelbooking.domain.entity.CarRental;
import com.example.travelbooking.domain.entity.Flight;
import com.example.travelbooking.domain.entity.Hotel;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
  private final InventoryService inventoryService;

  public AdminController(final InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @PostMapping("/flights")
  FlightDto createFlight(@RequestBody final Flight flight) {
    return inventoryService.seedFlight(flight);
  }

  @PostMapping("/hotels")
  HotelDto createHotel(@RequestBody final Hotel hotel) {
    return inventoryService.seedHotel(hotel);
  }

  @PostMapping("/car-rentals")
  CarRentalDto createCarRental(@RequestBody final CarRental carRental) {
    return inventoryService.seedCarRental(carRental);
  }
}

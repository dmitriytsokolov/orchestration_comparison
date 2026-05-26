package com.example.travelbooking.core.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelbooking.core.service.InventoryService;
import com.example.travelbooking.domain.dto.CarRentalDto;
import com.example.travelbooking.domain.dto.FlightDto;
import com.example.travelbooking.domain.dto.HotelDto;
import com.example.travelbooking.domain.dto.SearchCarRentalsRequest;
import com.example.travelbooking.domain.dto.SearchFlightsRequest;
import com.example.travelbooking.domain.dto.SearchHotelsRequest;

@RestController
@RequestMapping("/api/v1")
public class SearchController {
  private final InventoryService inventoryService;

  public SearchController(final InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping("/flights/search")
  List<FlightDto> searchFlights(
      @RequestParam final String origin,
      @RequestParam final String destination,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date,
      @RequestParam final int passengers) {
    return inventoryService.searchFlights(
        new SearchFlightsRequest(origin, destination, date, passengers));
  }

  @GetMapping("/hotels/search")
  List<HotelDto> searchHotels(
      @RequestParam final String city,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate checkIn,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate checkOut,
      @RequestParam final int rooms) {
    return inventoryService.searchHotels(new SearchHotelsRequest(city, checkIn, checkOut, rooms));
  }

  @GetMapping("/car-rentals/search")
  List<CarRentalDto> searchCarRentals(
      @RequestParam final String pickupLocation,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate pickupDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dropoffDate) {
    return inventoryService.searchCarRentals(
        new SearchCarRentalsRequest(pickupLocation, pickupDate, dropoffDate));
  }
}

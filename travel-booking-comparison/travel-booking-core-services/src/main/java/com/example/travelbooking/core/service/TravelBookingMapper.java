package com.example.travelbooking.core.service;

import org.mapstruct.Mapper;

import com.example.travelbooking.domain.dto.BookingResponse;
import com.example.travelbooking.domain.dto.BookingStatusResponse;
import com.example.travelbooking.domain.dto.CarRentalDto;
import com.example.travelbooking.domain.dto.FlightDto;
import com.example.travelbooking.domain.dto.HotelDto;
import com.example.travelbooking.domain.entity.Booking;
import com.example.travelbooking.domain.entity.CarRental;
import com.example.travelbooking.domain.entity.Flight;
import com.example.travelbooking.domain.entity.Hotel;

@Mapper(componentModel = "spring")
public interface TravelBookingMapper {
  FlightDto toDto(Flight flight);

  HotelDto toDto(Hotel hotel);

  CarRentalDto toDto(CarRental carRental);

  BookingResponse toResponse(Booking booking);

  BookingStatusResponse toStatusResponse(Booking booking);
}

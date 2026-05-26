CREATE TABLE users (
  id UUID PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE flights (
  id UUID PRIMARY KEY,
  flight_number VARCHAR(64) NOT NULL,
  origin VARCHAR(64) NOT NULL,
  destination VARCHAR(64) NOT NULL,
  departure_time TIMESTAMP NOT NULL,
  arrival_time TIMESTAMP NOT NULL,
  available_seats INTEGER NOT NULL,
  price_per_seat NUMERIC(12, 2) NOT NULL,
  currency VARCHAR(3) NOT NULL
);

CREATE TABLE hotels (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  city VARCHAR(128) NOT NULL,
  address VARCHAR(512) NOT NULL,
  check_in_date DATE NOT NULL,
  check_out_date DATE NOT NULL,
  available_rooms INTEGER NOT NULL,
  price_per_night NUMERIC(12, 2) NOT NULL,
  currency VARCHAR(3) NOT NULL
);

CREATE TABLE car_rentals (
  id UUID PRIMARY KEY,
  company VARCHAR(255) NOT NULL,
  model VARCHAR(255) NOT NULL,
  pickup_location VARCHAR(255) NOT NULL,
  dropoff_location VARCHAR(255) NOT NULL,
  pickup_date DATE NOT NULL,
  dropoff_date DATE NOT NULL,
  available BOOLEAN NOT NULL,
  price_per_day NUMERIC(12, 2) NOT NULL,
  currency VARCHAR(3) NOT NULL
);

CREATE TABLE bookings (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL REFERENCES users(id),
  status VARCHAR(32) NOT NULL,
  flight_id UUID NOT NULL REFERENCES flights(id),
  hotel_id UUID NOT NULL REFERENCES hotels(id),
  car_rental_id UUID NOT NULL REFERENCES car_rentals(id),
  total_amount NUMERIC(12, 2) NOT NULL,
  idempotency_key VARCHAR(255) NOT NULL UNIQUE,
  orchestration_id VARCHAR(255),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE payments (
  id UUID PRIMARY KEY,
  booking_id UUID NOT NULL REFERENCES bookings(id),
  amount NUMERIC(12, 2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  status VARCHAR(32) NOT NULL,
  idempotency_key VARCHAR(255) NOT NULL UNIQUE,
  gateway_ref VARCHAR(255)
);

CREATE TABLE booking_status_history (
  id UUID PRIMARY KEY,
  booking_id UUID NOT NULL REFERENCES bookings(id),
  status VARCHAR(32) NOT NULL,
  timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
  reason VARCHAR(512) NOT NULL
);

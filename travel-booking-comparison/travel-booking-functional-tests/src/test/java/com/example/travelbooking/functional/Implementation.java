package com.example.travelbooking.functional;

public enum Implementation {
  TEMPORAL("http://localhost:8080"),
  CAMUNDA("http://localhost:8082"),
  AXON("http://localhost:8083");

  private final String baseUrl;

  Implementation(final String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String baseUrl() {
    return baseUrl;
  }
}

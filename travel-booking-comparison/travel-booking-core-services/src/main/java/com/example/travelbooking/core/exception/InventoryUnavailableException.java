package com.example.travelbooking.core.exception;

public class InventoryUnavailableException extends RuntimeException {
  public InventoryUnavailableException(final String message) {
    super(message);
  }
}

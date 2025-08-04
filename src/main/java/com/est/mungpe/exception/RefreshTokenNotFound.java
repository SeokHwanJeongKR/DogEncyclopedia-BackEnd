package com.est.mungpe.exception;

public class RefreshTokenNotFound extends RuntimeException {
  public RefreshTokenNotFound(String message) {
    super(message);
  }
}

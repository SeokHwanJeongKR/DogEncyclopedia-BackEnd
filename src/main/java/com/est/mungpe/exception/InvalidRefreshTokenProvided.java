package com.est.mungpe.exception;

public class InvalidRefreshTokenProvided extends RuntimeException {
    public InvalidRefreshTokenProvided(String message) {
        super(message);
    }
}

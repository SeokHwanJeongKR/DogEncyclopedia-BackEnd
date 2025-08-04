package com.est.mungpe.exception;

public class EmptyRefreshToken extends RuntimeException {
    public EmptyRefreshToken(String message) {
        super(message);
    }
}

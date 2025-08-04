package com.est.mungpe.exception;

public class NoSameSenderAndReciverException extends RuntimeException {
    public NoSameSenderAndReciverException(String message) {
        super(message);
    }
}

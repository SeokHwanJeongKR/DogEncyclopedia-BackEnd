package com.est.mungpe.exception;

public class MaxFileLimitExceededException extends RuntimeException {
    public MaxFileLimitExceededException(String message) {
        super(message);
    }
}

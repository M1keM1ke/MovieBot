package ru.mike.moviebot.exception;

import lombok.ToString;

@ToString
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}

package com.musiclog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class MusiclogException extends RuntimeException {

    public final Map<String, String> validation = new HashMap<>();

    public MusiclogException(String message) {
        super(message);
    }

    public MusiclogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName,String message){
        validation.put(fieldName, message);
    }
}

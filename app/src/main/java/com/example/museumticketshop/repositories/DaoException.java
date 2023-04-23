package com.example.museumticketshop.repositories;

import androidx.annotation.Nullable;

public class DaoException extends Exception {
    private final String message;
    public DaoException(String message) {
        super(message);
        this.message = message;
    }

    @Nullable
    @Override
    public String getMessage() {
        return message;
    }
}

package com.clstephenson.mywgutracker.repositories;

public class EntityConstraintException extends RuntimeException {

    public EntityConstraintException(Throwable cause) {
        super(cause);
    }

    public EntityConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}

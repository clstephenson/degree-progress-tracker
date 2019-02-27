package com.clstephenson.mywgutracker.repositories;

public class AsyncTaskResult {
    private Result result;
    private EntityConstraintException constraintException = null;

    public AsyncTaskResult(Result result) {
        this.result = result;
    }

    public AsyncTaskResult(Result result, EntityConstraintException constraintException) {
        this.result = result;
        this.constraintException = constraintException;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public EntityConstraintException getConstraintException() {
        return constraintException;
    }

    public void setConstraintException(EntityConstraintException constraintException) {
        this.constraintException = constraintException;
    }

    public boolean isSuccessful() {
        if (result == Result.SUCCESS) {
            return true;
        }
        return false;
    }

    public enum Result {
        SUCCESS, FAIL
    }
}

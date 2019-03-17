package com.clstephenson.mywgutracker.repositories;

import androidx.annotation.Nullable;

public class DataTaskResult {
    private Result result;
    private DataTask.Action action;
    private EntityConstraintException constraintException = null;
    private Exception otherException = null;
    private Object data = null;

    public DataTaskResult(Result result) {
        this.result = result;
    }

    public DataTaskResult(DataTask.Action action, Result result) {
        this.action = action;
        this.result = result;
    }

    public DataTaskResult(DataTask.Action action, Result result, Object data) {
        this.action = action;
        this.result = result;
        this.data = data;
    }

    public DataTaskResult(DataTask.Action action, Result result, @Nullable EntityConstraintException constraintException,
                          @Nullable Exception otherException, @Nullable Object data) {
        this.action = action;
        this.result = result;
        this.constraintException = constraintException;
        this.otherException = otherException;
        this.data = data;
    }

    public DataTask.Action getAction() {
        return action;
    }

    public void setAction(DataTask.Action action) {
        this.action = action;
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

    public Exception getOtherException() {
        return otherException;
    }

    public void setOtherException(Exception otherException) {
        this.otherException = otherException;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccessful() {
        return result == Result.SUCCESS;
    }

    public enum Result {
        SUCCESS, FAIL
    }
}

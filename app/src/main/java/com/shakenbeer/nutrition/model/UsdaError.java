package com.shakenbeer.nutrition.model;

/**
 * Created by sviatoslav on 12/1/17.
 */

public class UsdaError {

    private int status;

    private String parameter;

    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

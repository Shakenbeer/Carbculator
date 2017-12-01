package com.shakenbeer.nutrition.model;


import java.util.List;

public class UsdaSearch {

    private UsdaList list;

    public UsdaList getList() {
        return list;
    }

    private UsdaErrors errors;

    public List<UsdaError> getErrors() {
        return errors == null ? null : errors.getErrors();
    }

    public String getErrorMessage() {
        assert errors != null;
        if (errors.getErrors() != null && errors.getErrors().size() > 0) {
            return errors.getErrors().get(0).getMessage();
        } else {
            return "Undefined server errors";
        }
    }
}
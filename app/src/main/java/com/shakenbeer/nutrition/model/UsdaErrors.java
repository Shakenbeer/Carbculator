package com.shakenbeer.nutrition.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sviatoslav on 12/1/17.
 */

public class UsdaErrors {

    @SerializedName("error")
    private List<UsdaError> errors;

    public List<UsdaError> getErrors() {
        return errors;
    }
}

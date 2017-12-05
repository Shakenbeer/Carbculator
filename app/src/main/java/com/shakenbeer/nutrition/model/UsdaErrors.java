package com.shakenbeer.nutrition.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


class UsdaErrors {

    @SerializedName("error")
    private List<UsdaError> errors;

    List<UsdaError> getErrors() {
        return errors;
    }
}

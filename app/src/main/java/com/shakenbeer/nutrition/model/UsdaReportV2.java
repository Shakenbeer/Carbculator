package com.shakenbeer.nutrition.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * https://ndb.nal.usda.gov/ndb/doc/apilist/API-FOOD-REPORT.md
 */
public class UsdaReportV2 {

    private int count;

    @SerializedName("notfound")
    private int notFound;

    private List<UsdaFood> foods;

    public int getCount() {
        return count;
    }

    public int getNotFound() {
        return notFound;
    }

    public List<UsdaFood> getFoods() {
        return foods;
    }
}

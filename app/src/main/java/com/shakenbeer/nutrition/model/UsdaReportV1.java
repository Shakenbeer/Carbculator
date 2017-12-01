package com.shakenbeer.nutrition.model;

/**
 * https://ndb.nal.usda.gov/ndb/doc/apilist/API-FOOD-REPORT.md
 */
public class UsdaReportV1 {

    private String sr;

    private String type;

    private UsdaFood_ food;

    public String getSr() {
        return sr;
    }

    public String getType() {
        return type;
    }

    public UsdaFood_ getFood() {
        return food;
    }
}

package com.shakenbeer.nutrition.model;

import java.util.List;

/**
 * https://ndb.nal.usda.gov/ndb/doc/apilist/API-FOOD-REPORT.md
 */
class UsdaFood_ {

    private Desc desc;

    private List<Nutrient> nutrients;

    public Desc getDesc() {
        return desc;
    }

    public List<Nutrient> getNutrients() {
        return nutrients;
    }
}

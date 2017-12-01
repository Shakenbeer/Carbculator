package com.shakenbeer.nutrition.model;

import com.google.gson.annotations.SerializedName;

/**
 * https://ndb.nal.usda.gov/ndb/doc/apilist/API-SEARCH.md
 */
public class UsdaItem {

    public static final String BRANDED_FOOD_PRODUCTS = "BL";

    public static final String STANDARD_RELEASE = "SR";

    private String group;

    private String name;

    private String ndbno;

    @SerializedName("ds")
    private String source;

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getNdbno() {
        return ndbno;
    }

    public String getSource() {
        return source;
    }
}

package com.shakenbeer.nutrition.model;


import com.google.gson.annotations.SerializedName;

public class Nutrient {

    public static final String ENERGY_ID = "208";

    public static final String PROTEIN_ID = "203";

    public static final String FAT_ID = "204";

    public static final String CARBS_ID = "206";

    @SerializedName("nutrient_id")
    private String id;

    private String name;

    private String unit;

    //float
    private String value;

    public boolean isEnergy() {
        return ENERGY_ID.equals(id);
    }

    public boolean isProtein() {
        return PROTEIN_ID.equals(id);
    }

    public boolean isFat() {
        return FAT_ID.equals(id);
    }

    public boolean isCarbs() {
        return CARBS_ID.equals(id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public String getValue() {
        return value;
    }
}

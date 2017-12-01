package com.shakenbeer.nutrition.model;


public enum UsdaDataSource {

    STANDARD("Standard Reference"),
    BRANDED("Branded Food Products");

    private final String name;

    UsdaDataSource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

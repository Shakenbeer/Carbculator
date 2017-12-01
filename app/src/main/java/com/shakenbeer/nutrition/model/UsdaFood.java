package com.shakenbeer.nutrition.model;


import java.util.List;

public class UsdaFood {

    private UsdaFood_ food;

    public Desc getDesc() {
        return food.getDesc();
    }

    public List<Nutrient> getNutrients() {
        return food.getNutrients();
    }
}

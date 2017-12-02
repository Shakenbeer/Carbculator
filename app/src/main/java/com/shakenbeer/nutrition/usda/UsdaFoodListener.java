package com.shakenbeer.nutrition.usda;


import com.shakenbeer.nutrition.model.Food;

public interface UsdaFoodListener {
    void onSelected(int position, Food food);
}

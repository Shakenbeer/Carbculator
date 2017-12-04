package com.shakenbeer.nutrition.usda;

import com.shakenbeer.nutrition.data.NutritionLab2;
import com.shakenbeer.nutrition.injection.FeatureScope;

import dagger.Module;
import dagger.Provides;

@Module
public class UsdaModule {

    @FeatureScope
    @Provides
    UsdaContract.Presenter provideUsdaPresenter(UsdaService usdaService, NutritionLab2 nutritionLab2) {
        return new UsdaPresenter(usdaService, nutritionLab2);
    }
}

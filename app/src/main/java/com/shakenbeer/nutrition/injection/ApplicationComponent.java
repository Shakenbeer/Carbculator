package com.shakenbeer.nutrition.injection;

import com.shakenbeer.nutrition.data.NutritionLab2;
import com.shakenbeer.nutrition.model.Storage;
import com.shakenbeer.nutrition.usda.UsdaService;

import javax.inject.Singleton;

import dagger.Component;

@SuppressWarnings("unused")
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Storage storage();

    NutritionLab2 nutritionLab2();

    UsdaService usdaService();
}

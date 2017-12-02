package com.shakenbeer.nutrition.usda;

import com.shakenbeer.nutrition.injection.ApplicationComponent;
import com.shakenbeer.nutrition.injection.FeatureScope;

import dagger.Component;

@FeatureScope
@Component(dependencies = ApplicationComponent.class, modules = UsdaModule.class)
public interface UsdaComponent {
    void inject(UsdaActivity usdaActivity);
}

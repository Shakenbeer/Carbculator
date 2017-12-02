package com.shakenbeer.nutrition.usda;

import com.shakenbeer.nutrition.injection.FeatureScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class UsdaModule {

    @FeatureScope
    @Provides
    UsdaContract.Presenter provideUsdaPresenter(UsdaService usdaService) {
        return new UsdaPresenter(usdaService);
    }
}

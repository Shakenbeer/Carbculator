package com.shakenbeer.nutrition.usda;

import com.shakenbeer.nutrition.injection.FeatureScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class UsdaModule {

    @Provides
    @Singleton
    UsdaService provideUsdaService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nal.usda.gov/ndb/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(UsdaService.class);
    }

    @FeatureScope
    @Provides
    UsdaContract.Presenter provideUsdaPresenter(UsdaService usdaService) {
        return new UsdaPresenter(usdaService);
    }
}

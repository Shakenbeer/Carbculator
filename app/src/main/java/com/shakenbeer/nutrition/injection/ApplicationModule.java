package com.shakenbeer.nutrition.injection;

import android.app.Application;
import android.content.Context;

import com.shakenbeer.nutrition.db.DbStorage;
import com.shakenbeer.nutrition.model.Storage;
import com.shakenbeer.nutrition.usda.UsdaService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    Storage provideStorage(Context context) {
        return new DbStorage(context);
    }

    @Provides
    @Singleton
    UsdaService provideUsdaService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nal.usda.gov/ndb/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(UsdaService.class);
    }
}

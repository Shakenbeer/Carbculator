package com.shakenbeer.nutrition.usda;


import com.shakenbeer.nutrition.model.UsdaReportV2;
import com.shakenbeer.nutrition.model.UsdaSearch;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsdaService {

    @GET("search/?format=json&sort=r&api_key=HOKb3GuLe3ujjPXmYYhfHubLMTUygwiljngF87ak")
    Single<UsdaSearch> search(@Query("q") String query,
                              @Query("ds") String source,
                              @Query("max") int count,
                              @Query("offset") int offset);

    @GET("V2/reports?format=json&type=b&api_key=HOKb3GuLe3ujjPXmYYhfHubLMTUygwiljngF87ak")
    Single<UsdaReportV2> report(@Query("ndbno") List<String> ndbno);
}

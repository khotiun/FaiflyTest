package com.khotiun.android.faiflytest.rest.api;

import com.google.gson.JsonObject;
import com.khotiun.android.faiflytest.model.pojo.Example;
import com.khotiun.android.faiflytest.tools.Config;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ApiService {

    @GET(Config.COUNTRIES_GET)
    Observable<JsonObject> get();

    @GET(Config.CITY_GET)
    Observable<Example> getCity(@Query("q") String city,
                                @Query("maxRows") int rows,
                                @Query("username") String userName);
}

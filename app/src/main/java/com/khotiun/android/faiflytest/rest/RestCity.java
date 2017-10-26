package com.khotiun.android.faiflytest.rest;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.khotiun.android.faiflytest.rest.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestCity {

    //constant BASE_URL
    private static final String BASE_URL = "http://api.geonames.org/";

    //Get Retrofit Instance
    @NonNull
    private static Retrofit getRetrofitInstance() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//для получения observable вместо call
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    //Get ApiService
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}

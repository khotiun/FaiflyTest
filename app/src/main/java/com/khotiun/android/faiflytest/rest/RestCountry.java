package com.khotiun.android.faiflytest.rest;

import android.support.annotation.NonNull;

import com.khotiun.android.faiflytest.rest.api.ApiService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestCountry {

    //constant BASE_URL
    private static final String BASE_URL = "https://raw.githubusercontent.com/";

    //Get Retrofit Instance
    @NonNull
    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//для получения observable вместо call
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    //Get ApiService
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}

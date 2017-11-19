package com.khotiun.android.faiflytest.model;

import com.khotiun.android.faiflytest.model.pojo.Example;
import com.khotiun.android.faiflytest.rest.RestCity;

import rx.Observable;

/**
 * Created by hotun on 04.11.2017.
 */

public class ModelCity implements IModelCity {

    @Override
    public Observable<Example> getCity (String city, int rows, String userName) {
        return RestCity.getApiService().getCity(city, rows, userName);
    }
}

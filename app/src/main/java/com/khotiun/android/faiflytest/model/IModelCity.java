package com.khotiun.android.faiflytest.model;

import com.khotiun.android.faiflytest.model.pojo.Example;

import rx.Observable;

/**
 * Created by hotun on 04.11.2017.
 */

public interface IModelCity {

    Observable<Example> getCity (String city, int rows, String userName);
}

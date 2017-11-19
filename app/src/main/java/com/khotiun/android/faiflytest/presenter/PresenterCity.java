package com.khotiun.android.faiflytest.presenter;

import android.util.Log;

import com.khotiun.android.faiflytest.model.IModelCity;
import com.khotiun.android.faiflytest.model.ModelCity;
import com.khotiun.android.faiflytest.tools.Config;
import com.khotiun.android.faiflytest.view.IViewCity;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PresenterCity implements IPresenterCity {

    private static final String TAG = "PresenterCity";


    private IModelCity model;
    private IViewCity view;


    public PresenterCity(IViewCity view) {
        model = new ModelCity();
        this.view = view;
    }

    @Override
    public void getDetailCity(String city) {
        model.getCity(city, 1, Config.userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(example -> {
                            String title = example.getGeonames().get(0).getTitle();
                            String summary = example.getGeonames().get(0).getSummary();
                            double lat = example.getGeonames().get(0).getLat();
                            double lng = example.getGeonames().get(0).getLng();
                            Log.d(TAG, lat + "");
                            Log.d(TAG, lng + "");
                            view.showDetailCity(title, summary, lat, lng);
                        },
                        e -> Log.d(TAG, e.getMessage()));

    }
}

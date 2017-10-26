package com.khotiun.android.faiflytest.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.khotiun.android.faiflytest.model.Country;
import com.khotiun.android.faiflytest.model.CountryLab;
import com.khotiun.android.faiflytest.model.IModelCountry;
import com.khotiun.android.faiflytest.view.IViewCountry;

import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class PresenterCountry implements IPresenterCountry {

    private static final String TAG = "PresenterCountry";

    private IViewCountry view;
    private IModelCountry model;

    private Gson gson = new Gson();

    public PresenterCountry(IViewCountry view, CountryLab countryLab) {
        this.view = view;
        model = countryLab;
    }

    @Override
    public void getCountries() {
        Log.d(TAG, "getCountries()");
        model.getCountries(new CountryLab.LoadCountriesCallback() {
            @Override
            public void onLoad(List<String> countries) {
                view.setDataToSpiner(countries);
            }
        });
    }

    @Override
    public void getCities(String country) {
        model.getCities(new CountryLab.LoadCitiesCallback() {
            @Override
            public void onLoadCities(List<String> cities) {
                view.showListCities(cities);
            }
        }, country);
    }

    @Override
    public void saveDataToDb() {
        model.getJson()
                .flatMap(new Func1<JsonObject, Observable<Map.Entry<String, JsonElement>>>() {
                    @Override
                    public Observable<Map.Entry<String, JsonElement>> call(JsonObject jsonObject) {
                        return Observable.from(jsonObject.entrySet());
                    }
                })
                .filter(stringCountry -> !stringCountry.getKey().equals(""))
                .map(stringJsonElementEntry -> {
                    final String strCountry = stringJsonElementEntry.getKey();
                    final String strCities = gson.toJson(stringJsonElementEntry.getValue());
                    createCountry(strCountry, strCities);
                    return stringJsonElementEntry;
                }).toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonObject -> {
                    Log.d(TAG, " start ");
                    getCountries();
                    view.hideProgressBar();
                    Log.d(TAG, " end ");
                }, e -> Log.d(TAG, e.getMessage()));
    }

    //for create country of JSON
    private Country createCountry(String strCountry, String cities) {
        Country country = new Country();
        country.setCountryName(strCountry);
        country.setCitiesList(cities);
        model.addCountry(country);
        return country;
    }
}

package com.khotiun.android.faiflytest.model;

import com.google.gson.JsonObject;

import rx.Observable;

public interface IModelCountry {

    void addCountry(Country c);
    void getCountries(CountryLab.LoadCountriesCallback callback);
    void getCities(CountryLab.LoadCitiesCallback callback, String country);
    Observable<JsonObject> getJson();

}

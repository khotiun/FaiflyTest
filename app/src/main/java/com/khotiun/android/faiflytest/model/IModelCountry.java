package com.khotiun.android.faiflytest.model;

import com.google.gson.JsonObject;

import rx.Observable;

public interface IModelCountry {

    void addCountry(Country c);
    void getCountries(ModelCountryLab.LoadCountriesCallback callback);
    void getCities(ModelCountryLab.LoadCitiesCallback callback, String country);
    Observable<JsonObject> getJson();

}

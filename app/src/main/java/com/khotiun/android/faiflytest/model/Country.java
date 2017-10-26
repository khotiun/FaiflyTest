package com.khotiun.android.faiflytest.model;

public class Country {

    private String countryName;
    private String citiesList;

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(String citiesList) {
        this.citiesList = citiesList;
    }

    @Override
    public String toString() {
        return "Country{" +
                "countryName='" + countryName + '\'' +
                '}';
    }
}

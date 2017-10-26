package com.khotiun.android.faiflytest.view;

import java.util.List;

public interface IViewCountry {

    void showListCities(List<String> cities);
    void setDataToSpiner(List<String> countries);
    void showProgressBar();
    void hideProgressBar();

}

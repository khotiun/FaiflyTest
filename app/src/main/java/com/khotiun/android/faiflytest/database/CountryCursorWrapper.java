package com.khotiun.android.faiflytest.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.khotiun.android.faiflytest.model.Country;

public class CountryCursorWrapper extends CursorWrapper {

    public CountryCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    //for take city name
    public Country getCountry() {
        String coutryName = getString(getColumnIndex(CountryDbSchema.CountryTable.Cols.COUNTRY));
        String cities = getString(getColumnIndex(CountryDbSchema.CountryTable.Cols.CITY));
        Country country = new Country();
        country.setCountryName(coutryName);
        country.setCitiesList(cities);

        return country;
    }
}

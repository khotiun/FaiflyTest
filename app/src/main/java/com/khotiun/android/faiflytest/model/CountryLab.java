package com.khotiun.android.faiflytest.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.khotiun.android.faiflytest.database.CountryBaseHelper;
import com.khotiun.android.faiflytest.database.CountryCursorWrapper;
import com.khotiun.android.faiflytest.rest.RestCountry;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

import static com.khotiun.android.faiflytest.database.CountryDbSchema.CountryTable;

//singleton
public class CountryLab implements IModelCountry {

    private static CountryLab sCountryLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    // there can be only one object of this class
    public static CountryLab getCountryLab(Context context) {
        if (sCountryLab == null) {
            sCountryLab = new CountryLab(context);
        }
        return sCountryLab;
    }

    private CountryLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CountryBaseHelper(mContext).getWritableDatabase();//get bd for write
    }

    public interface LoadCountriesCallback {
        void onLoad(List<String> countries);
    }

    public interface LoadCitiesCallback {
        void onLoadCities(List<String> cities);
    }

    @Override
    public void addCountry(Country country) {
//        new AddCountry(country).execute();
        ContentValues values = getContentValues(country);
        mDatabase.insert(CountryTable.NAME, null, values);
    }

    @Override
    public void getCountries(LoadCountriesCallback callback) {
        new LoadCountries(callback).execute();
    }

    @Override
    public void getCities(LoadCitiesCallback callback, String country) {
        new LoadCities(callback, country).execute();
    }

    private static ContentValues getContentValues(Country country) {
        ContentValues values = new ContentValues();
        values.put(CountryTable.Cols.COUNTRY, country.getCountryName());
        values.put(CountryTable.Cols.CITY, country.getCitiesList());

        return values;
    }

    //read bd
    private CountryCursorWrapper queryCountry(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CountryTable.NAME,
                null, // Columns - null select all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new CountryCursorWrapper(cursor);
    }

    @Override
    public Observable<JsonObject> getJson() {
        return RestCountry.getApiService().get();
    }

    class LoadCountries extends AsyncTask<Void, Void, List<String>> {

        private final LoadCountriesCallback callback;

        LoadCountries(LoadCountriesCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> countries = new ArrayList<>();

            CountryCursorWrapper cursor = queryCountry(null, null);

            try {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    countries.add(cursor.getCountry().getCountryName());
                    cursor.moveToNext();
                }
            } finally {
                cursor.close();
            }
            Collections.sort(countries);

            return countries;
        }

        @Override
        protected void onPostExecute(List<String> countries) {
            if (callback != null) {
                callback.onLoad(countries);
            }
        }
    }

    class LoadCities extends AsyncTask<Void, Void, List<String>> {

        private String country;
        private final LoadCitiesCallback callback;

        public LoadCities(LoadCitiesCallback callback, String country) {
            this.country = country;
            this.callback = callback;
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            CountryCursorWrapper cursor = queryCountry(
                    CountryTable.Cols.COUNTRY + " = ?",
                    new String[]{country}
            );

            try {
                if (cursor.getCount() == 0) {
                    return null;
                }

                cursor.moveToFirst();
                String cities = cursor.getCountry().getCitiesList();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                Gson gson = new Gson();
                List<String> listCities = gson.fromJson(cities, type);
                return listCities;
            } finally {
                cursor.close();
            }
        }

        @Override
        protected void onPostExecute(List<String> cities) {
            if (callback != null) {
                callback.onLoadCities(cities);
            }
        }
    }
}

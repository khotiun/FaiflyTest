package com.khotiun.android.faiflytest.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.khotiun.android.faiflytest.R;
import com.khotiun.android.faiflytest.model.CountryLab;
import com.khotiun.android.faiflytest.model.pojo.Example;
import com.khotiun.android.faiflytest.presenter.IPresenterCountry;
import com.khotiun.android.faiflytest.presenter.PresenterCountry;
import com.khotiun.android.faiflytest.rest.RestCity;
import com.khotiun.android.faiflytest.tools.Config;
import com.khotiun.android.faiflytest.view.IViewCountry;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements IViewCountry {

    private static final String TAG = "MainActivity";

    IPresenterCountry mPresenter;
    private ProgressBar mProgressBar;
    private Spinner mSpinner;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        setSupportActionBar(mToolbar);

        if (mPresenter == null) {
            mPresenter = new PresenterCountry(this, CountryLab.getCountryLab(this));
        }

        if (isDatabaseEmpty()){
            showProgressBar();
            mPresenter.saveDataToDb();
            dbIsFull();
        } else {
            mPresenter.getCountries();
        }
    }

    private void init() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showListCities(List<String> cities) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        CityAdapter cityAdapter = new CityAdapter(cities);
        mRecyclerView.setAdapter(cityAdapter);
    }

    @Override
    public void setDataToSpiner(List<String> countries) {
        countries.add(0, getString(R.string.choose_the_country));
        // Вызываем адаптер
//        mSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries));
        mSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner, R.id.item_spinner_view, countries));
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mSpinner.getSelectedItem().toString().equals(getString(R.string.choose_the_country))) {
                    mPresenter.getCities(mSpinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    // Проверка на заполненность БД.
    private boolean isDatabaseEmpty() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        return defaultSharedPreferences.getBoolean(Config.SPREF_IS_DATABASE_EMPTY, true);

    }

    private void dbIsFull() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(Config.SPREF_IS_DATABASE_EMPTY, false)
                .apply();
    }

    private class CityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleView;
        private String cityName;

        public void bindCity(String cityName) {
            this.cityName = cityName;
            mTitleView.setText(cityName);
        }

        public CityHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            mTitleView = (TextView) itemView.findViewById(R.id.city_view);
        }

        @Override
        public void onClick(View v) {

            RestCity.getApiService().getCity(cityName, 1, Config.userName)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Example>() {
                                   @Override
                                   public void call(Example example) {
                                       Log.d(TAG, "subscribe " + example.getGeonames().get(0).getTitle());
                                       Log.d(TAG, example.getGeonames().get(0).getTitle());
                                   }
                               },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            });
        }
    }

    public class CityAdapter extends RecyclerView.Adapter<CityHolder> {

        private List<String> mCityList;

        public CityAdapter(List<String> cityNames) {
            mCityList = cityNames;
        }

        @Override
        public CityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CityHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_city, parent, false));
        }

        @Override
        public void onBindViewHolder(CityHolder holder, int position) {
            String cityName = mCityList.get(position);
            holder.bindCity(cityName);
        }

        @Override
        public int getItemCount() {
            return mCityList.size();
        }
    }
}

package com.khotiun.android.faiflytest.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.khotiun.android.faiflytest.R;
import com.khotiun.android.faiflytest.presenter.IPresenterCity;
import com.khotiun.android.faiflytest.presenter.PresenterCity;
import com.khotiun.android.faiflytest.view.IViewCity;

public class DetailCityActivity extends AppCompatActivity implements IViewCity, NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "DetailCityActivity";
    private static final String EXTRA_CITY = "com.khotiun.android.faiflytest.city";
    private double lat, lng;
    private String title;

    private IPresenterCity mPresenter;
    private MapFragment mapFragment;

    private TextView cityTitleView, citySummaryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_city);

        init();

        mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map_view);//находим наш фрагмент
        mapFragment.getMapAsync(this);//асинхронно загружаем карту на наш фрагмент


        if (mPresenter == null) {
            mPresenter = new PresenterCity(this);
        }

        mPresenter.getDetailCity(getIntent().getStringExtra(EXTRA_CITY));
    }

    private void init() {
        cityTitleView = (TextView) findViewById(R.id.city_name);
        citySummaryView = (TextView) findViewById(R.id.city_summary);
    }

    public static Intent detailIntent(Context context, String city) {

        Intent i = new Intent(context, DetailCityActivity.class);
        i.putExtra(EXTRA_CITY, city);
        return i;
    }

    @Override
    public void showError() {

    }

    @Override
    public void showDetailCity(String cityName, String citySammary, double lat, double lng) {
        Log.d(TAG, cityName);
        cityTitleView.setText(cityName);
        citySummaryView.setText(citySammary);
        this.lat = lat;
        this.lng = lng;
        this.title = cityName;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap map) {

        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        map.setTrafficEnabled(true);
        map.setIndoorEnabled(true);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);//кнопки увеличения и уменьшения карты

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //LocationManager - нужен для получения местонахождения
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager//получаем обьект с которого можно достать координаты нашего местонахождения
                .getBestProvider(criteria, false));

        double latLocation = location.getLatitude();
        double lngLocation = location.getLongitude();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latLocation, lngLocation))
//                .zoom(10)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);
        Log.d(TAG, lat + "");
        Log.d(TAG, lng + "");
        map.addMarker(new MarkerOptions()//добавляем маркер на карту
                        .position(new LatLng(lat, lng))//задаем место нахождения маркера
                        .title(title)//название маркера
//                        .snippet("18th century building")//какой то текст под заголовком
                        .draggable(false)//возможность перетаскивать маркер
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))//делает маячек синим
        );

        map.setOnMarkerClickListener(this);//устанавливаем слушателя
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, lat + "");
        Log.d(TAG, lng + "");
        return false;
    }
}

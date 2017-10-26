package com.khotiun.android.faiflytest;


import android.app.Application;

import com.khotiun.android.faiflytest.di.component.ApplicationComponent;
import com.khotiun.android.faiflytest.di.component.DaggerApplicationComponent;
import com.khotiun.android.faiflytest.di.module.ApplicationModule;

/**
 * Created by hotun on 01.10.2017.
 */

public class MyApplication extends Application {

    private static ApplicationComponent sApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initComponent();
    }

    //метод для инициализации компонента
    private void initComponent() {
        //DaggerApplicationComponent - этот класс генерируется в процессе компиляции проекта. Кодогенерация.
        sApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();
    }

    //метод для получения компонента
    public static ApplicationComponent getApplicationComponent() {
        return sApplicationComponent;
    }
}

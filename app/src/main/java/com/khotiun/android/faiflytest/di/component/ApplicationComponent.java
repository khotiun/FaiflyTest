package com.khotiun.android.faiflytest.di.component;

import com.khotiun.android.faiflytest.di.module.ApplicationModule;
import com.khotiun.android.faiflytest.di.module.CountryLabModule;
import com.khotiun.android.faiflytest.presenter.PresenterCountry;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, CountryLabModule.class}) //связывает модули частямии, которые запрашивают зависимости, в данном случае один компонент отвечает за два модуля
public interface ApplicationComponent {

    //presenters
    void inject(PresenterCountry presenter);
}

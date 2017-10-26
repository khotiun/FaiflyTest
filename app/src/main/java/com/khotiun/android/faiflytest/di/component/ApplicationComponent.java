package com.khotiun.android.faiflytest.di.component;

import com.khotiun.android.faiflytest.di.module.ApplicationModule;
import com.khotiun.android.faiflytest.di.module.RestModule;
import com.khotiun.android.faiflytest.presenter.PresenterCountry;
import com.khotiun.android.faiflytest.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by hotun on 05.10.2017.
 */

@Singleton
@Component(modules = {ApplicationModule.class, RestModule.class}) //связывает модули частямии, которые запрашивают зависимости, в данном случае один компонент отвечает за два модуля
public interface ApplicationComponent {

    //методы где будет использоваться внедрение
    //activities
//    void inject(BaseActivity activity);
    void inject(MainActivity activity);

//    //fragments
//    void inject(NewsFeedFragment fragment);
//    //holders
//    void inject(NewsItemBodyHolder holder);
//    void inject(NewsItemFooterHolder holder);
//
    //presenters
    void inject(PresenterCountry presenter);

}

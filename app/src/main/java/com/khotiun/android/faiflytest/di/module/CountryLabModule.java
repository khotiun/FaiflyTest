package com.khotiun.android.faiflytest.di.module;

import android.content.Context;

import com.khotiun.android.faiflytest.model.CountryLab;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CountryLabModule {

    @Singleton
    @Provides
    public CountryLab provideCountryLab(Context context) {
        return new CountryLab(context);
    }
}

package org.nearbyshops.core.DaggerModules;

import android.app.Application;


import org.nearbyshops.core.MyApplicationCoreNew;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumeet on 14/5/16.
 */

@Module
public class AppModule {

    MyApplicationCoreNew mApplication;

    public AppModule(MyApplicationCoreNew application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

}

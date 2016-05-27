package org.nearbyshops.enduser.DaggerModules;


import org.nearbyshops.enduser.DataProvidersRetrofit.ItemCategoryRetrofitProvider;
import org.nearbyshops.enduser.DataProvidersRetrofit.ItemProviderRetrofit;
import org.nearbyshops.enduser.DataProvidersRetrofit.ShopRetrofitProvider;
import org.nearbyshops.enduser.DataRouters.ItemCategoryDataRouter;
import org.nearbyshops.enduser.Database.FakeDAO;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.StandardInterfaces.DataProviderItem;
import org.nearbyshops.enduser.StandardInterfaces.DataProviderItemCategory;
import org.nearbyshops.enduser.StandardInterfaces.DataProviderShop;
import org.nearbyshops.enduser.StandardInterfacesGeneric.DataSubscriber;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sumeet on 19/5/16.
 */

@Module
public class DataModule {

    final static public String NETWORK_DATA_PROVIDER = "NetworkDataProvider";
    final static public String DB_DATA_PROVIDER = "DBDataProvider";

    public DataModule() {
    }

    @Provides
    @Singleton
    @Named(DataModule.NETWORK_DATA_PROVIDER)
    DataProviderItemCategory providesNetworkDataProvider()
    {
        return new ItemCategoryRetrofitProvider();
    }



    @Provides
    @Singleton
    DataSubscriber<ItemCategory> providesDBDataSubscriber()
    {
        return FakeDAO.getInstance();
    }


    @Provides
    @Singleton
    @Named(DataModule.DB_DATA_PROVIDER)
    DataProviderItemCategory providesDBDataProvider()
    {
        return FakeDAO.getInstance();
    }


    @Provides
    @Singleton
    ItemCategoryDataRouter providesDataRouter(DataSubscriber<ItemCategory> dbDataSubscriber)
    {
        return new ItemCategoryDataRouter();
    }

    @Provides
    @Singleton
    DataProviderShop providesDataProviderShop()
    {
        return new ShopRetrofitProvider();
    }


    @Provides
    @Singleton
    DataProviderItem providesDataProviderItem()
    {
        return new ItemProviderRetrofit();
    }


}

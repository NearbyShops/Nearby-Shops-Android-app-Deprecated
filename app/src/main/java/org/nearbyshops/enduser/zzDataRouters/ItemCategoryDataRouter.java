package org.nearbyshops.enduser.zzDataRouters;


import org.nearbyshops.enduser.zaDeprecatedItemCategories.DaggerComponentBuilder;
import org.nearbyshops.enduser.DaggerModules.DataModule;
import org.nearbyshops.enduser.Model.ItemCategory;
import org.nearbyshops.enduser.zzStandardInterfaces.DataProviderItemCategory;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataRouter;
import org.nearbyshops.enduser.zzStandardInterfacesGeneric.DataSubscriber;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by sumeet on 19/5/16.
 */
public class ItemCategoryDataRouter implements DataRouter<ItemCategory> {


    @Inject @Named(DataModule.NETWORK_DATA_PROVIDER)
    DataProviderItemCategory networkDataProvider;

    @Inject @Named(DataModule.DB_DATA_PROVIDER)
    DataProviderItemCategory dbDataProvider;

    @Inject
    DataSubscriber<ItemCategory> dbDataSubscriber;


    public ItemCategoryDataRouter() {

        DaggerComponentBuilder.getInstance()
                .getDataComponent()
                .Inject(this);

        // save the recieved data into the database whenever the network call is made
        networkDataProvider.subscribe(dbDataSubscriber);
    }


    public ItemCategoryDataRouter(DataProviderItemCategory networkDataProvider,
                                  DataProviderItemCategory dbDataProvider,
                                  DataSubscriber<ItemCategory> dbDataSubscriber)

    {
        this.networkDataProvider = networkDataProvider;
        this.dbDataProvider = dbDataProvider;
        this.dbDataSubscriber = dbDataSubscriber;

        networkDataProvider.subscribe(dbDataSubscriber);
    }



    @Override
    public DataProviderItemCategory getDataProvider() {


        return networkDataProvider;

        /*
        if(UtilityGeneral.isNetworkAvailable(MyApplication.getAppContext()))
        {
            // network is available . Make a network call using network data router
            return networkDataProvider;
        }

        else
        {
            // network is not available.
            return dbDataProvider;
        }

        */

    }

}
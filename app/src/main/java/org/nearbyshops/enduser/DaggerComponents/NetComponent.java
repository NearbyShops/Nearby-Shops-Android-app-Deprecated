package org.nearbyshops.enduser.DaggerComponents;


import org.nearbyshops.enduser.DaggerModules.AppModule;
import org.nearbyshops.enduser.DaggerModules.NetModule;
import org.nearbyshops.enduser.DataProvidersRetrofit.ItemCategoryRetrofitProvider;
import org.nearbyshops.enduser.DataProvidersRetrofit.ItemProviderRetrofit;
import org.nearbyshops.enduser.DataProvidersRetrofit.ShopRetrofitProvider;
import org.nearbyshops.enduser.ShopsForItems.ShopsForItem;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {

    // void inject(MyFragment fragment);
    // void inject(MyService service);

    void inject(ItemCategoryRetrofitProvider provider);

    void inject(ShopRetrofitProvider provider);

    void inject(ItemProviderRetrofit providerRetrofit);

    void Inject(ShopsForItem shopsForItem);
}

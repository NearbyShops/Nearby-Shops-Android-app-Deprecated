package org.nearbyshops.enduser.DaggerComponents;


import org.nearbyshops.enduser.Carts.CartItemListActivity;
import org.nearbyshops.enduser.Carts.CartsListActivity;
import org.nearbyshops.enduser.DaggerModules.AppModule;
import org.nearbyshops.enduser.DaggerModules.NetModule;
import org.nearbyshops.enduser.DataProvidersRetrofit.ItemCategoryRetrofitProvider;
import org.nearbyshops.enduser.DataProvidersRetrofit.ItemProviderRetrofit;
import org.nearbyshops.enduser.DataProvidersRetrofit.ShopRetrofitProvider;
import org.nearbyshops.enduser.ShopsForItems.AdapterFilledCarts;
import org.nearbyshops.enduser.ShopsForItems.AdapterNewCarts;
import org.nearbyshops.enduser.ShopsForItems.FilledCartsFragment;
import org.nearbyshops.enduser.ShopsForItems.NewCartsFragment;

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

    void Inject(FilledCartsFragment shopsForItem);

    void Inject(NewCartsFragment newCartsFragment);

    void Inject(AdapterNewCarts adapterNewCarts);

    void Inject(AdapterFilledCarts adapterFilledCarts);

    void Inject(CartsListActivity cartsListActivity);

    void Inject(CartItemListActivity cartItemListActivity);

}

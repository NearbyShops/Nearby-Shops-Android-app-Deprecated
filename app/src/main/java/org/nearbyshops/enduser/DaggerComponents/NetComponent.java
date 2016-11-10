package org.nearbyshops.enduser.DaggerComponents;


import org.nearbyshops.enduser.DeliveryAddress.AddAddressActivity;
import org.nearbyshops.enduser.Carts.CartItemListActivity;
import org.nearbyshops.enduser.Carts.CartsListActivity;
import org.nearbyshops.enduser.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduser.DeliveryAddress.EditAddressActivity;
import org.nearbyshops.enduser.Carts.PlaceOrderActivity;
import org.nearbyshops.enduser.DaggerModules.AppModule;
import org.nearbyshops.enduser.DaggerModules.NetModule;
import org.nearbyshops.enduser.FilterShopDialog.FilterShopsDialog;
import org.nearbyshops.enduser.ItemCategoryOption.FragmentShopOld;
import org.nearbyshops.enduser.ItemDetail.ItemDetail;
import org.nearbyshops.enduser.ItemDetail.RateReviewItemDialog;
import org.nearbyshops.enduser.Items.ItemsList.FragmentItemsList;
import org.nearbyshops.enduser.ItemsByCategory.ItemCategories.ItemCategoriesFragmentItem;
import org.nearbyshops.enduser.ItemsByCategory.Items.FragmentItem_ItemByCategory;
import org.nearbyshops.enduser.ItemsByCategoryScreenTwo.ItemCategories.ItemCategoriesHorizontal;
import org.nearbyshops.enduser.ItemsByCategoryScreenTwo.Items.FragmentItemScreenHorizontal;
import org.nearbyshops.enduser.ItemsInShop.ShopItems.AdapterItemsInShop;
import org.nearbyshops.enduser.ItemsInShop.ShopItems.FragmentItemsInShop;
import org.nearbyshops.enduser.Login.LoginDialog;
import org.nearbyshops.enduser.Login.LoginServiceDialog;
import org.nearbyshops.enduser.LoginActivity;
import org.nearbyshops.enduser.ShopDetail.ShopDetail;
import org.nearbyshops.enduser.ShopDetail.RateReviewDialog;
import org.nearbyshops.enduser.ShopItemByShop.ItemCategories.ItemCategoriesFragmentShopHome;
import org.nearbyshops.enduser.ShopItemByShop.ShopItems.AdapterShopItems;
import org.nearbyshops.enduser.ShopItemByShop.ShopItems.FragmentShopItemsByShop;
import org.nearbyshops.enduser.ShopReview.ShopReviewAdapter;
import org.nearbyshops.enduser.ShopReview.ShopReviewStats;
import org.nearbyshops.enduser.ShopReview.ShopReviews;
import org.nearbyshops.enduser.Shops.ListFragment.FragmentShopTwo;
import org.nearbyshops.enduser.ShopsByCategory.ItemCategories.ItemCategoriesFragment;
import org.nearbyshops.enduser.ShopsByCategory.Shops.FragmentShop;
import org.nearbyshops.enduser.ItemCategoryOption.FragmentItemCategories;
import org.nearbyshops.enduser.ItemCategoryOption.FragmentItem;
import org.nearbyshops.enduser.ShopItemByItem.FilledCarts.AdapterFilledCarts;
import org.nearbyshops.enduser.ShopItemByItem.NewCarts.AdapterNewCarts;
import org.nearbyshops.enduser.ShopItemByItem.FilledCarts.FilledCartsFragment;
import org.nearbyshops.enduser.ShopItemByItem.NewCarts.NewCartsFragment;

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

//    void inject(ItemCategoryRetrofitProvider provider);
//
//    void inject(ShopRetrofitProvider provider);
//
//    void inject(ItemProviderRetrofit providerRetrofit);

    void Inject(FilledCartsFragment shopsForItem);

    void Inject(NewCartsFragment newCartsFragment);

    void Inject(AdapterNewCarts adapterNewCarts);

    void Inject(AdapterFilledCarts adapterFilledCarts);

    void Inject(CartsListActivity cartsListActivity);

    void Inject(CartItemListActivity cartItemListActivity);

    void Inject(DeliveryAddressActivity deliveryAddressActivity);

    void Inject(AddAddressActivity addAddressActivity);

    void Inject(EditAddressActivity editAddressActivity);

    void Inject(PlaceOrderActivity placeOrderActivity);

    void Inject(FragmentItemCategories fragmentItemCategories);

    void Inject(LoginActivity loginActivity);

    void Inject(FragmentItem itemFragment);

    void Inject(FragmentShopOld shopFragment);

    void Inject(LoginDialog loginDialog);

    void Inject(ItemCategoriesFragment itemCategoriesFragment);

    void Inject(FragmentShop fragmentShop);

    void Inject(ItemCategoriesFragmentItem itemCategoriesFragmentItem);

    void Inject(FragmentItem_ItemByCategory fragmentItem_itemByCategory);

    void Inject(LoginServiceDialog loginServiceDialog);

    void Inject(ShopDetail shopDetail);

    void Inject(RateReviewDialog rateReviewDialog);

    void Inject(ShopReviews shopReviews);

    void Inject(ItemCategoriesFragmentShopHome itemCategoriesFragmentShopHome);

    void Inject(FragmentShopItemsByShop fragmentShopItemsByShop);

    void Inject(ShopReviewAdapter shopReviewAdapter);

    void Inject(ItemDetail itemDetail);

    void Inject(RateReviewItemDialog rateReviewItemDialog);

    void Inject(ShopReviewStats shopReviewStats);

    void Inject(AdapterShopItems adapterShopItems);

    void Inject(FragmentShopTwo fragmentShopTwo);

    void Inject(FragmentItemsList fragmentItemsList);

    void Inject(FragmentItemsInShop fragmentItemsInShop);

    void Inject(AdapterItemsInShop adapterItemsInShop);

    void Inject(ItemCategoriesHorizontal itemCategoriesHorizontal);

    void Inject(FragmentItemScreenHorizontal fragmentItemScreenHorizontal);

    void Inject(FilterShopsDialog filterShopsDialog);
}

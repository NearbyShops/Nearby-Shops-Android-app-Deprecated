package org.nearbyshops.enduserapp.DaggerComponents;


import org.nearbyshops.enduserapp.CancelledOrders.CancelledByEndUser.FragmentCancelledByUser;
import org.nearbyshops.enduserapp.CancelledOrders.CancelledByShop.FragmentCancelledByShop;
import org.nearbyshops.enduserapp.CancelledOrders.ReturnedByDeliveryGuy.FragmentReturnedByDG;
import org.nearbyshops.enduserapp.Carts.CartItem.CartItemAdapter;
import org.nearbyshops.enduserapp.DeliveryAddress.EditAddress.EditAddressFragment;
import org.nearbyshops.enduserapp.DeliveryAddress.Previous.AddAddressActivity;
import org.nearbyshops.enduserapp.Carts.CartItem.CartItemListActivity;
import org.nearbyshops.enduserapp.Carts.CartsList.CartsListFragment;
import org.nearbyshops.enduserapp.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduserapp.DeliveryAddress.Previous.EditAddressActivity;
import org.nearbyshops.enduserapp.Carts.PlaceOrderActivity;
import org.nearbyshops.enduserapp.DaggerModules.AppModule;
import org.nearbyshops.enduserapp.DaggerModules.NetModule;
import org.nearbyshops.enduserapp.EditProfileEndUser.EditEndUserFragment;
import org.nearbyshops.enduserapp.FilterItemsBySpecifications.FilterItemsFragment;
import org.nearbyshops.enduserapp.FilterShopDialog.FilterShopsDialog;
import org.nearbyshops.enduserapp.FilterShops.FilterShops;
import org.nearbyshops.enduserapp.Home;
import org.nearbyshops.enduserapp.HomeNew.HomeNew;
import org.nearbyshops.enduserapp.ItemsByCategoryTypeSimple.AdapterSimple;
import org.nearbyshops.enduserapp.ItemsByCategoryTypeSimple.ItemCategoriesFragmentSimple;
import org.nearbyshops.enduserapp.ItemCategoryOption.FragmentShopOld;
import org.nearbyshops.enduserapp.ItemDetail.ItemDetail;
import org.nearbyshops.enduserapp.ItemDetail.RateReviewItemDialog;
import org.nearbyshops.enduserapp.Items.ItemsList.FragmentItemsList;
import org.nearbyshops.enduserapp.ItemsByCategorySwipe.ItemCategories.ItemCategoriesFragmentItem;
import org.nearbyshops.enduserapp.ItemsByCategorySwipe.Items.FragmentItem_ItemByCategory;
import org.nearbyshops.enduserapp.ItemsByCategoryHorizontal.ItemCategories.ItemCategoriesHorizontal;
import org.nearbyshops.enduserapp.ItemsByCategoryHorizontal.Items.FragmentItemScreenHorizontal;
import org.nearbyshops.enduserapp.ItemsInShop.ShopItems.FragmentItemsInShop;
import org.nearbyshops.enduserapp.ItemsInShopByCat.AdapterItemsInShop;
import org.nearbyshops.enduserapp.ItemsInShopByCat.ItemsInShopByCatFragment;
import org.nearbyshops.enduserapp.LoginActivity;
import org.nearbyshops.enduserapp.LoginNew.LoginFragment;
import org.nearbyshops.enduserapp.LoginNew.ServiceIndicatorFragment;
import org.nearbyshops.enduserapp.OrderDetail.FragmentOrderDetail;
import org.nearbyshops.enduserapp.OrderDetailPFS.FragmentOrderDetailPFS;
import org.nearbyshops.enduserapp.OrderHistoryHD.OrderHistoryHD.Complete.CompleteOrdersFragment;
import org.nearbyshops.enduserapp.OrderHistoryHD.OrderHistoryHD.Pending.PendingOrdersFragment;
import org.nearbyshops.enduserapp.OrderHistoryNew.PendingOrdersFragmentNew;
import org.nearbyshops.enduserapp.OrderHistoryPFS.Complete.CompleteOrdersFragmentPFS;
import org.nearbyshops.enduserapp.OrderHistoryPFS.Pending.PendingOrdersFragmentPFS;
import org.nearbyshops.enduserapp.OrdersCancelledPFS.CancelledByShop.CancelledByShopFragmentPFS;
import org.nearbyshops.enduserapp.OrdersCancelledPFS.CancelledByUser.CancelledByUserFragmentPFS;
import org.nearbyshops.enduserapp.ShopDetail.ShopDetail;
import org.nearbyshops.enduserapp.ShopDetail.RateReviewDialog;
import org.nearbyshops.enduserapp.ShopItemByShopOld.ItemCategories.ItemCategoriesFragmentShopHome;
import org.nearbyshops.enduserapp.ShopItemByShopOld.ShopItems.AdapterShopItems;
import org.nearbyshops.enduserapp.ShopItemByShopOld.ShopItems.FragmentShopItemsByShop;
import org.nearbyshops.enduserapp.ShopReview.ShopReviewAdapter;
import org.nearbyshops.enduserapp.ShopReview.ShopReviewStats;
import org.nearbyshops.enduserapp.ShopReview.ShopReviews;
import org.nearbyshops.enduserapp.Shops.ListFragment.FragmentShopTwo;
import org.nearbyshops.enduserapp.ShopsByCatSimple.ShopsByCatFragment;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.ItemCategories.ItemCategoriesFragment;
import org.nearbyshops.enduserapp.ShopsByCategoryOld.Shops.FragmentShop;
import org.nearbyshops.enduserapp.ItemCategoryOption.FragmentItemCategories;
import org.nearbyshops.enduserapp.ItemCategoryOption.FragmentItem;
import org.nearbyshops.enduserapp.ShopItemByItem.FilledCarts.AdapterFilledCarts;
import org.nearbyshops.enduserapp.ShopItemByItem.NewCarts.AdapterNewCarts;
import org.nearbyshops.enduserapp.ShopItemByItem.FilledCarts.FilledCartsFragment;
import org.nearbyshops.enduserapp.ShopItemByItem.NewCarts.NewCartsFragment;
import org.nearbyshops.enduserapp.SignUp.ForgotPassword.FragmentCheckResetCode;
import org.nearbyshops.enduserapp.SignUp.ForgotPassword.FragmentEnterCredentials;
import org.nearbyshops.enduserapp.SignUp.ForgotPassword.FragmentResetPassword;
import org.nearbyshops.enduserapp.SignUp.FragmentEmailOrPhone;
import org.nearbyshops.enduserapp.SignUp.FragmentEnterPassword;
import org.nearbyshops.enduserapp.SignUp.FragmentVerifyEmailSignUp;


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

    void Inject(CartsListFragment cartsListActivity);

    void Inject(CartItemListActivity cartItemListActivity);

    void Inject(DeliveryAddressActivity deliveryAddressActivity);

    void Inject(AddAddressActivity addAddressActivity);

    void Inject(EditAddressActivity editAddressActivity);

    void Inject(PlaceOrderActivity placeOrderActivity);

    void Inject(FragmentItemCategories fragmentItemCategories);

    void Inject(LoginActivity loginActivity);

    void Inject(FragmentItem itemFragment);

    void Inject(FragmentShopOld shopFragment);


    void Inject(ItemCategoriesFragment itemCategoriesFragment);

    void Inject(FragmentShop fragmentShop);

    void Inject(ItemCategoriesFragmentItem itemCategoriesFragmentItem);

    void Inject(FragmentItem_ItemByCategory fragmentItem_itemByCategory);


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

    void Inject(org.nearbyshops.enduserapp.ItemsInShop.ShopItems.AdapterItemsInShop adapterItemsInShop);

    void Inject(ItemCategoriesHorizontal itemCategoriesHorizontal);

    void Inject(FragmentItemScreenHorizontal fragmentItemScreenHorizontal);

    void Inject(FilterShopsDialog filterShopsDialog);

    void Inject(ItemCategoriesFragmentSimple itemCategoriesFragmentSimple);

    void Inject(AdapterSimple adapterSimple);

    void Inject(ItemsInShopByCatFragment itemsInShopByCatFragment);

    void Inject(AdapterItemsInShop adapterItemsInShop);

    void Inject(FilterShops filterShops);

    void Inject(ShopsByCatFragment shopsByCatFragment);

    void Inject(CompleteOrdersFragment completeOrdersFragment);

    void Inject(PendingOrdersFragment pendingOrdersFragment);

    void Inject(FragmentOrderDetail fragmentOrderDetail);

    void Inject(FragmentCancelledByUser fragmentCancelledByUser);

    void Inject(FragmentCancelledByShop fragmentCancelledByShop);

    void Inject(FragmentReturnedByDG fragmentReturnedByDG);

    void Inject(CompleteOrdersFragmentPFS completeOrdersFragmentPFS);

    void Inject(FragmentOrderDetailPFS fragmentOrderDetailPFS);

    void Inject(PendingOrdersFragmentPFS pendingOrdersFragmentPFS);

    void Inject(CancelledByShopFragmentPFS cancelledByShopFragmentPFS);

    void Inject(CancelledByUserFragmentPFS cancelledByUserFragmentPFS);

    void Inject(EditEndUserFragment editEndUserFragment);

    void Inject(EditAddressFragment editAddressFragment);

    void Inject(Home home);

    void Inject(FilterItemsFragment filterItemsFragment);

    void Inject(LoginFragment loginFragment);

    void Inject(FragmentCheckResetCode fragmentCheckResetCode);

    void Inject(FragmentEmailOrPhone fragmentEmailOrPhone);

    void Inject(FragmentEnterCredentials fragmentEnterCredentials);

    void Inject(FragmentEnterPassword fragmentEnterPassword);

    void Inject(FragmentResetPassword fragmentResetPassword);

    void Inject(FragmentVerifyEmailSignUp fragmentVerifyEmailSignUp);


    void Inject(HomeNew homeNew);

    void Inject(ServiceIndicatorFragment serviceIndicatorFragment);

    void Inject(PendingOrdersFragmentNew pendingOrdersFragmentNew);

    void Inject(CartItemAdapter cartItemAdapter);
}

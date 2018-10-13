package org.nearbyshops.enduserappnew.DaggerComponents;


import org.nearbyshops.enduserappnew.CancelledOrders.CancelledByEndUser.FragmentCancelledByUser;
import org.nearbyshops.enduserappnew.CancelledOrders.CancelledByShop.FragmentCancelledByShop;
import org.nearbyshops.enduserappnew.CancelledOrders.ReturnedByDeliveryGuy.FragmentReturnedByDG;
import org.nearbyshops.enduserappnew.Carts.CartItem.CartItemAdapter;
import org.nearbyshops.enduserappnew.DeliveryAddress.EditAddress.EditAddressFragment;
import org.nearbyshops.enduserappnew.DeliveryAddress.Previous.AddAddressActivity;
import org.nearbyshops.enduserappnew.Carts.CartItem.CartItemListActivity;
import org.nearbyshops.enduserappnew.Carts.CartsList.CartsListFragment;
import org.nearbyshops.enduserappnew.DeliveryAddress.DeliveryAddressActivity;
import org.nearbyshops.enduserappnew.DeliveryAddress.Previous.EditAddressActivity;
import org.nearbyshops.enduserappnew.Carts.PlaceOrderActivity;
import org.nearbyshops.enduserappnew.DaggerModules.AppModule;
import org.nearbyshops.enduserappnew.DaggerModules.NetModule;
import org.nearbyshops.enduserappnew.EditProfile.ChangeEmail.FragmentChangeEmail;
import org.nearbyshops.enduserappnew.EditProfile.ChangeEmail.FragmentVerifyEmailChange;
import org.nearbyshops.enduserappnew.EditProfile.ChangePassword.FragmentChangePassword;
import org.nearbyshops.enduserappnew.EditProfile.ChangePhone.FragmentChangePhone;
import org.nearbyshops.enduserappnew.EditProfile.ChangePhone.FragmentVerifyPhone;
import org.nearbyshops.enduserappnew.EditProfile.FragmentEditProfile;
import org.nearbyshops.enduserappnew.FilterItemsBySpecifications.FilterItemsFragment;
import org.nearbyshops.enduserappnew.FilterShopDialog.FilterShopsDialog;
import org.nearbyshops.enduserappnew.Home;
import org.nearbyshops.enduserappnew.ItemImages.ItemImageListFragment;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.AdapterSimple;
import org.nearbyshops.enduserappnew.ItemsByCategoryTypeSimple.ItemCategoriesFragmentSimple;
import org.nearbyshops.enduserappnew.ItemByCategory.FragmentShop;
import org.nearbyshops.enduserappnew.ItemDetail.ItemDetail;
import org.nearbyshops.enduserappnew.ItemDetail.RateReviewItemDialog;
import org.nearbyshops.enduserappnew.Items.ItemsList.FragmentItemsList;
import org.nearbyshops.enduserappnew.ItemsByCategorySwipe.ItemCategories.ItemCategoriesFragmentItem;
import org.nearbyshops.enduserappnew.ItemsByCategorySwipe.Items.FragmentItem_ItemByCategory;
import org.nearbyshops.enduserappnew.ItemsByCategoryHorizontal.ItemCategories.ItemCategoriesHorizontal;
import org.nearbyshops.enduserappnew.ItemsByCategoryHorizontal.Items.FragmentItemScreenHorizontal;
import org.nearbyshops.enduserappnew.ItemsInShop.ShopItems.FragmentItemsInShop;
import org.nearbyshops.enduserappnew.ItemsInShopByCat.AdapterItemsInShop;
import org.nearbyshops.enduserappnew.ItemsInShopByCat.ItemsInShopByCatFragment;
import org.nearbyshops.enduserappnew.Login.LoginFragment;
import org.nearbyshops.enduserappnew.Login.ServiceIndicatorFragment;
import org.nearbyshops.enduserappnew.OneSignal.UpdateOneSignalID;
import org.nearbyshops.enduserappnew.OrderDetail.FragmentOrderDetail;
import org.nearbyshops.enduserappnew.OrderHistoryHD.OrderHistoryHD.Complete.CompleteOrdersFragment;
import org.nearbyshops.enduserappnew.OrderHistoryHD.OrderHistoryHD.Pending.PendingOrdersFragment;
import org.nearbyshops.enduserappnew.OrderHistoryNew.PendingOrdersFragmentNew;
import org.nearbyshops.enduserappnew.ShopDetail.ShopDetail;
import org.nearbyshops.enduserappnew.ShopDetail.RateReviewDialog;
import org.nearbyshops.enduserappnew.ShopImages.ShopImageListFragment;
import org.nearbyshops.enduserappnew.ShopItemByItemNew.ShopItemFragment.AdapterShopItem;
import org.nearbyshops.enduserappnew.ShopItemByItemNew.ShopItemFragment.ShopItemFragment;
import org.nearbyshops.enduserappnew.ShopItemByShop.ItemCategories.ItemCategoriesFragmentShopHome;
import org.nearbyshops.enduserappnew.ShopItemByShop.ShopItems.AdapterShopItems;
import org.nearbyshops.enduserappnew.ShopItemByShop.ShopItems.FragmentShopItemsByShop;
import org.nearbyshops.enduserappnew.ShopReview.ShopReviewAdapter;
import org.nearbyshops.enduserappnew.ShopReview.ShopReviewStats;
import org.nearbyshops.enduserappnew.ShopReview.ShopReviews;
import org.nearbyshops.enduserappnew.Shops.ListFragment.FragmentShopNew;
import org.nearbyshops.enduserappnew.ShopsByCatSimple.ShopsByCatFragment;
import org.nearbyshops.enduserappnew.ShopsByCategory.ItemCategories.ItemCategoriesFragment;
import org.nearbyshops.enduserappnew.ItemByCategory.FragmentItemCategories;
import org.nearbyshops.enduserappnew.ItemByCategory.FragmentItem;
import org.nearbyshops.enduserappnew.ShopItemByItem.FilledCarts.AdapterFilledCarts;
import org.nearbyshops.enduserappnew.ShopItemByItem.NewCarts.AdapterNewCarts;
import org.nearbyshops.enduserappnew.ShopItemByItem.FilledCarts.FilledCartsFragment;
import org.nearbyshops.enduserappnew.ShopItemByItem.NewCarts.NewCartsFragment;
import org.nearbyshops.enduserappnew.SignUp.ForgotPassword.FragmentCheckResetCode;
import org.nearbyshops.enduserappnew.SignUp.ForgotPassword.FragmentEnterCredentials;
import org.nearbyshops.enduserappnew.SignUp.ForgotPassword.FragmentResetPassword;
import org.nearbyshops.enduserappnew.SignUp.FragmentEmailOrPhone;
import org.nearbyshops.enduserappnew.SignUp.FragmentEnterPassword;
import org.nearbyshops.enduserappnew.SignUp.FragmentVerifyEmailSignUp;
import org.nearbyshops.enduserappnew.TabProfile.ProfileFragment;


import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sumeet on 14/5/16.
 */

@Singleton
@Component(modules={AppModule.class, NetModule.class})
public interface NetComponent {


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

    void Inject(FragmentItem itemFragment);

    void Inject(FragmentShop shopFragment);


    void Inject(ItemCategoriesFragment itemCategoriesFragment);

    void Inject(org.nearbyshops.enduserappnew.ShopsByCategory.Shops.FragmentShop fragmentShop);

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

    void Inject(FragmentShopNew fragmentShopTwo);

    void Inject(FragmentItemsList fragmentItemsList);

    void Inject(FragmentItemsInShop fragmentItemsInShop);

    void Inject(org.nearbyshops.enduserappnew.ItemsInShop.ShopItems.AdapterItemsInShop adapterItemsInShop);

    void Inject(ItemCategoriesHorizontal itemCategoriesHorizontal);

    void Inject(FragmentItemScreenHorizontal fragmentItemScreenHorizontal);

    void Inject(FilterShopsDialog filterShopsDialog);

    void Inject(ItemCategoriesFragmentSimple itemCategoriesFragmentSimple);

    void Inject(AdapterSimple adapterSimple);

    void Inject(ItemsInShopByCatFragment itemsInShopByCatFragment);

    void Inject(AdapterItemsInShop adapterItemsInShop);



    void Inject(ShopsByCatFragment shopsByCatFragment);

    void Inject(CompleteOrdersFragment completeOrdersFragment);

    void Inject(PendingOrdersFragment pendingOrdersFragment);

    void Inject(FragmentOrderDetail fragmentOrderDetail);

    void Inject(FragmentCancelledByUser fragmentCancelledByUser);

    void Inject(FragmentCancelledByShop fragmentCancelledByShop);

    void Inject(FragmentReturnedByDG fragmentReturnedByDG);

    void Inject(EditAddressFragment editAddressFragment);

    void Inject(FilterItemsFragment filterItemsFragment);

    void Inject(LoginFragment loginFragment);

    void Inject(FragmentCheckResetCode fragmentCheckResetCode);

    void Inject(FragmentEmailOrPhone fragmentEmailOrPhone);

    void Inject(FragmentEnterCredentials fragmentEnterCredentials);

    void Inject(FragmentEnterPassword fragmentEnterPassword);

    void Inject(FragmentResetPassword fragmentResetPassword);

    void Inject(FragmentVerifyEmailSignUp fragmentVerifyEmailSignUp);


    void Inject(Home homeNew);

    void Inject(ServiceIndicatorFragment serviceIndicatorFragment);

    void Inject(PendingOrdersFragmentNew pendingOrdersFragmentNew);

    void Inject(CartItemAdapter cartItemAdapter);

    void Inject(FragmentVerifyEmailChange fragmentVerifyEmailChange);

    void Inject(FragmentVerifyPhone fragmentVerifyPhone);

    void Inject(FragmentChangePassword fragmentChangePassword);

    void Inject(FragmentChangePhone fragmentChangePhone);

    void Inject(FragmentEditProfile fragmentEditProfile);

    void Inject(ProfileFragment profileFragment);

    void Inject(FragmentChangeEmail fragmentChangeEmail);

    void Inject(UpdateOneSignalID updateOneSignalID);

    void Inject(ItemImageListFragment imageListFragment);

    void Inject(ShopImageListFragment shopImageListFragment);

    void Inject(ShopItemFragment shopItemFragment);

    void Inject(AdapterShopItem adapterShopItem);
}

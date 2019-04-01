package org.nearbyshops.enduserappnew.RetrofitRESTContract;

import org.nearbyshops.enduserappnew.ModelCartOrder.CartItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by sumeet on 31/5/16.
 */
public interface CartItemService {


    @GET("/api/CartItem")
    Call<List<CartItem>> getCartItem(@Query("CartID")Integer cartID,
                                     @Query("ItemID")Integer itemID,
                                     @Query("EndUserID") Integer endUserID,
                                     @Query("ShopID") Integer shopID,
                                     @Query("GetItems") Boolean getItems);

    @DELETE("/api/CartItem")
    Call<ResponseBody> deleteCartItem(@Query("CartID")int cartID, @Query("ItemID") int itemID,
                                      @Query("EndUserID") int endUserID,
                                      @Query("ShopID") int shopID);


    @PUT("/api/CartItem")
    Call<ResponseBody> updateCartItem(@Body CartItem cartItem,
                                      @Query("EndUserID") int endUserID, @Query("ShopID") int shopID);

    @POST("/api/CartItem")
    Call<ResponseBody> createCartItem(@Body CartItem cartItem,
                                      @Query("EndUserID") int endUserID, @Query("ShopID") int shopID);


}

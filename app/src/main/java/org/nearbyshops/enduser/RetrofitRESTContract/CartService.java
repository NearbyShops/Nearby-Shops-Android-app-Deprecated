package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.Model.Cart;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 31/5/16.
 */
public interface CartService {

    @GET("/api/Cart")
    Call<List<Cart>> getCarts(@Query("EndUserID") int endUserID, @Query("ShopID")int shopID);

    @GET("/api/Cart/{id}")
    Call<Cart> getCart(@Path("id") int cart_id);

    @POST("/api/Cart")
    Call<Cart> postCart(@Body Cart cart);

    @PUT("/api/Cart/{id}")
    Call<ResponseBody> putCart(@Body Cart cart, @Path("id") int cart_id);

    @DELETE("/api/Cart/{id}")
    Call<ResponseBody> deleteCart(@Path("id") int cart_id);

}

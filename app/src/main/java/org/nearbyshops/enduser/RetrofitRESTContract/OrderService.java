package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.Model.Cart;
import org.nearbyshops.enduser.Model.Order;

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
public interface OrderService {

    @GET("/api/Order")
    Call<List<Order>> getOrders(@Query("EndUserID") int endUserID, @Query("ShopID") int shopID);

    @GET("/api/Order/{OrderID}")
    Call<Order> getOrder(@Path("OrderID") int orderID);

    @POST("/api/Order")
    Call<Order> postOrder(@Body Order order,@Query("CartID") int cartID);

    @PUT("/api/Order/{OrderID}")
    Call<ResponseBody> putOrder(@Body Order order, @Path("OrderID") int orderID);

}

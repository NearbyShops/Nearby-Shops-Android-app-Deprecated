package org.nearbyshops.enduser.RetrofitRESTContract;

import org.nearbyshops.enduser.ModelCartOrder.Endpoints.OrderEndPoint;
import org.nearbyshops.enduser.ModelCartOrder.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by sumeet on 31/5/16.
 */
public interface OrderService {

    @POST("/api/Order")
    Call<Order> postOrder(@Body Order order,@Query("CartID") int cartID);


    @GET("/api/Order")
    Call<OrderEndPoint> getOrders(@Header("Authorization") String headers,
                                  @Query("OrderID")Integer orderID,
                                  @Query("ShopID")Integer shopID,
                                  @Query("PickFromShop") Boolean pickFromShop,
                                  @Query("StatusHomeDelivery")Integer homeDeliveryStatus,
                                  @Query("StatusPickFromShopStatus")Integer pickFromShopStatus,
                                  @Query("DeliveryGuyID")Integer deliveryGuyID,
                                  @Query("PaymentsReceived") Boolean paymentsReceived,
                                  @Query("DeliveryReceived") Boolean deliveryReceived,
                                  @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
                                  @Query("PendingOrders") Boolean pendingOrders,
                                  @Query("SearchString") String searchString,
                                  @Query("SortBy") String sortBy,
                                  @Query("Limit")Integer limit, @Query("Offset")Integer offset,
                                  @Query("metadata_only")Boolean metaonly);
}

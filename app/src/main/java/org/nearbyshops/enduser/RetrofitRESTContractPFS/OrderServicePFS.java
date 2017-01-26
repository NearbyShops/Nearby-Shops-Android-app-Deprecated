package org.nearbyshops.enduser.RetrofitRESTContractPFS;

import org.nearbyshops.enduser.ModelCartOrder.Endpoints.OrderEndPoint;
import org.nearbyshops.enduser.ModelCartOrder.Order;
import org.nearbyshops.enduser.ModelPickFromShop.OrderEndPointPFS;
import org.nearbyshops.enduser.ModelPickFromShop.OrderItemEndPointPFS;
import org.nearbyshops.enduser.ModelPickFromShop.OrderPFS;

import javax.annotation.security.RolesAllowed;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sumeet on 31/5/16.
 */
public interface OrderServicePFS {


    @POST("/api/OrderPFS")
    Call<ResponseBody> postOrder(@Body OrderPFS order, @Query("CartID") int cartID);



    @GET("/api/OrderPFS")
    Call<OrderEndPointPFS> getOrders(@Header("Authorization") String headers,
                                      @Query("OrderID")Integer orderID,
                                      @Query("ShopID")Integer shopID,
                                      @Query("StatusPickFromShopStatus")Integer pickFromShopStatus,
                                      @Query("PaymentsReceived") Boolean paymentsReceived,
                                      @Query("DeliveryReceived") Boolean deliveryReceived,
                                      @Query("latCenter")Double latCenter, @Query("lonCenter")Double lonCenter,
                                      @Query("PendingOrders") Boolean pendingOrders,
                                      @Query("SearchString") String searchString,
                                      @Query("SortBy") String sortBy,
                                      @Query("Limit")Integer limit, @Query("Offset")Integer offset,
                                      @Query("metadata_only")Boolean metaonly);




    @PUT("/api/OrderPFS/CancelByUser/{OrderID}")
    Call<ResponseBody> cancelledByEndUser(@Header("Authorization") String headers,
                                          @Path("OrderID") int orderID);




    // order Item Endpoint

    @GET("/api/OrderItemPFS")
    Call<OrderItemEndPointPFS> getOrderItemPFS(@Header("Authorization") String headers,
                                               @Query("OrderID")Integer orderID,
                                               @Query("ItemID")Integer itemID,
                                               @Query("SearchString")String searchString,
                                               @Query("SortBy") String sortBy,
                                               @Query("Limit")Integer limit, @Query("Offset")Integer offset,
                                               @Query("metadata_only")Boolean metaonly);

}

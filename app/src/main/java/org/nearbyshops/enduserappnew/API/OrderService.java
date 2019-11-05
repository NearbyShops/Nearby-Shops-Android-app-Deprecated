package org.nearbyshops.enduserappnew.API;

import okhttp3.ResponseBody;
import org.nearbyshops.enduserappnew.Model.ModelEndPoints.OrderEndPoint;
import org.nearbyshops.enduserappnew.Model.ModelCartOrder.Order;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by sumeet on 31/5/16.
 */
public interface OrderService {




    @POST("/api/Order")
    Call<ResponseBody> postOrder(@Body Order order, @Query("CartID") int cartID);



    @GET("/api/Order")
    Call<OrderEndPoint> getOrders(@Header("Authorization") String headers,
                                  @Query("OrderID") Integer orderID,
                                  @Query("ShopID") Integer shopID,
                                  @Query("FilterByUserID") boolean filterByUserID,
                                  @Query("PickFromShop") Boolean pickFromShop,
                                  @Query("StatusHomeDelivery") Integer homeDeliveryStatus,
                                  @Query("StatusPickFromShopStatus") Integer pickFromShopStatus,
                                  @Query("DeliveryGuyID") Integer deliveryGuyID,
                                  @Query("latCenter") Double latCenter, @Query("lonCenter") Double lonCenter,
                                  @Query("PendingOrders") Boolean pendingOrders,
                                  @Query("SearchString") String searchString,
                                  @Query("SortBy") String sortBy,
                                  @Query("Limit") Integer limit, @Query("Offset") Integer offset,
                                  @Query("metadata_only") Boolean metaonly);




    @PUT("/api/Order/CancelByUser/{OrderID}")
    Call<ResponseBody> cancelledByEndUser(@Header("Authorization") String headers,
                                          @Path("OrderID") int orderID);





}

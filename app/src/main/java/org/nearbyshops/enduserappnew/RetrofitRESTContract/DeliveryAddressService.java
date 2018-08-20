package org.nearbyshops.enduserappnew.RetrofitRESTContract;

import org.nearbyshops.enduserappnew.ModelStats.DeliveryAddress;

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
public interface DeliveryAddressService {

    @GET("/api/DeliveryAddress")
    Call<List<DeliveryAddress>> getAddresses(@Query("EndUserID") int endUserID);

    @GET("/api/DeliveryAddress/{id}")
    Call<DeliveryAddress> getAddress(@Path("id") int address_id);

    @POST("/api/DeliveryAddress")
    Call<DeliveryAddress> postAddress(@Body DeliveryAddress address);

    @PUT("/api/DeliveryAddress/{id}")
    Call<ResponseBody> putAddress(@Body DeliveryAddress address, @Path("id") int address_id);

    @DELETE("/api/DeliveryAddress/{id}")
    Call<ResponseBody> deleteAddress(@Path("id") int address_id);

}

package org.nearbyshops.enduser.RetrofitRESTContract;


import org.nearbyshops.enduser.Model.Service;

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
 * Created by sumeet on 12/3/16.
 */
public interface ServiceConfigurationService {

    @GET("/api/Service")
    Call<List<Service>> getServices(@Query("ServiceLevel") int serviceLevel,
                                    @Query("ServiceType") int serviceType,
                                    @Query("LatCenter") int latCenter,
                                    @Query("LonCenter") double lonCenter,
                                    @Query("SortBy") String sortBy,
                                    @Query("Limit") int limit, @Query("Offset") int offset);


    @GET("/api/Service/{id}")
    Call<Service> getService(@Path("id") int id);

    @POST("/api/Service")
    Call<Service> postService(@Body Service service);

    @PUT("/api/Service/{id}")
    Call<ResponseBody> putService(@Body Service service, @Path("id") int id);

    @DELETE("/api/Service/{id}")
    Call<ResponseBody> deleteService(@Path("id") int id);

}

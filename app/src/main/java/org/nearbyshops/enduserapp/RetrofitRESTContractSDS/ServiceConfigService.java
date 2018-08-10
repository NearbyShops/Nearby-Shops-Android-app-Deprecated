package org.nearbyshops.enduserapp.RetrofitRESTContractSDS;


import org.nearbyshops.enduserapp.Model.Image;
import org.nearbyshops.enduserapp.ModelServiceConfig.Endpoints.ServiceConfigurationEndPoint;
import org.nearbyshops.enduserapp.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserapp.ModelServiceConfig.ServiceConfigurationLocal;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by sumeet on 12/3/16.
 */
public interface ServiceConfigService {




    @GET("/api/v1/ServiceConfiguration")
    Call<ServiceConfigurationLocal> getService(@Query("latCenter")Double latCenter,
                                               @Query("lonCenter")Double lonCenter);




    // Image Calls

    @POST("/api/v1/ServiceConfiguration/Image")
    Call<Image> uploadImage(@Header("Authorization") String headers,
                            @Body RequestBody image);

    //@QueryParam("PreviousImageName") String previousImageName


    @DELETE("/api/v1/ServiceConfiguration/Image/{name}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String headers,
                                   @Path("name") String fileName);

}

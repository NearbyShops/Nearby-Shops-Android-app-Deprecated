package org.nearbyshops.enduserappnew.RetrofitRESTContract;

import org.nearbyshops.enduserappnew.Model.Image;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by sumeet on 29/3/16.
 */
public interface ImageService {


    @POST("/api/Images")
    Call<Image> uploadImage(@Body RequestBody image);

    @Multipart
    @POST("/api/Images")
    Call<Image> uploadImageMultipart(@Part MultipartBody.Part file);

    @DELETE("/api/Images/{name}")
    Call<ResponseBody> deleteImage(@Path("name") String fileName);

}
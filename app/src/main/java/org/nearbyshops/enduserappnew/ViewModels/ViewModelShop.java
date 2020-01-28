package org.nearbyshops.enduserappnew.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import org.nearbyshops.enduserappnew.API.ShopService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewModelShop extends AndroidViewModel {

    private MutableLiveData<List<Object>> datasetLive;
    private List<Object> dataset;
    private MutableLiveData<Integer> event;
    private MutableLiveData<String> message;


    public static int EVENT_BECOME_A_SELLER_SUCCESSFUL = 1;
    public static int EVENT_ = 2;
    public static int EVENT_NETWORK_FAILED = 3;





    @Inject
    Gson gson;


    @Inject
    ShopService shopService;







    public ViewModelShop(@NonNull Application application) {
        super(application);

        event = new MutableLiveData<>();
        message = new MutableLiveData<>();
        datasetLive = new MutableLiveData<>();
        dataset = new ArrayList<>();


        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }





    public MutableLiveData<List<Object>> getData()
    {
        return datasetLive;
    }





    public LiveData<Integer> getEvent()
    {

        return event;
    }





    public LiveData<String> getMessage()
    {

        return message;
    }




    public void becomeASeller()
    {

        Call<ResponseBody> call = shopService.becomeASeller(PrefLogin.getAuthorizationHeaders(getApplication()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {

                    User user = PrefLogin.getUser(getApplication());
                    user.setRole(User.ROLE_SHOP_ADMIN_CODE);
                    PrefLogin.saveUserProfile(user,getApplication());


                    message.postValue("Successful !");
                    event.postValue(ViewModelShop.EVENT_BECOME_A_SELLER_SUCCESSFUL);
                }
                else
                {
                    message.postValue("Failed Code : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                message.postValue("Failed ! Check your network connection !");
            }
        });

    }


}



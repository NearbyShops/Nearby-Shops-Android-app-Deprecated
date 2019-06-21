package org.nearbyshops.enduserappnew.Markets.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import okhttp3.OkHttpClient;
import org.nearbyshops.enduserappnew.API.LoginUsingOTPService;
import org.nearbyshops.enduserappnew.API.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Markets.Interfaces.listItemMarketNotifications;
import org.nearbyshops.enduserappnew.Model.ModelRoles.User;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.Model.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import java.util.Currency;
import java.util.Locale;

public class ViewHolderSavedMarket extends RecyclerView.ViewHolder {


    @BindView(R.id.market_photo) ImageView marketPhoto;
    @BindView(R.id.market_name) TextView marketName;
    @BindView(R.id.market_city) TextView marketCity;

    @BindView(R.id.progress_bar_select) ProgressBar progressBarSelect;
    @BindView(R.id.select_market) TextView selectMarket;



    private ServiceConfigurationGlobal configurationGlobal;
    private Context context;


    private listItemMarketNotifications subscriber;


    @Inject Gson gson;






    public static ViewHolderSavedMarket create(ViewGroup parent, Context context, listItemMarketNotifications subscriber)
    {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_market_type_two,parent,false);

        return new ViewHolderSavedMarket(view,context,subscriber);
    }




    public ViewHolderSavedMarket(@NonNull View itemView, Context context, listItemMarketNotifications subscriber) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.context = context;
        this.subscriber = subscriber;

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }




    public void setItem(ServiceConfigurationGlobal item)
    {

        this.configurationGlobal = item;


        marketName.setText(configurationGlobal.getServiceName());
        marketCity.setText(configurationGlobal.getCity());


        String imagePath = PrefServiceConfig.getServiceURL_SDS(context)
                + "/api/v1/ServiceConfiguration/Image/three_hundred_" + configurationGlobal.getLogoImagePath() + ".jpg";


//                System.out.println("Service LOGO : " + imagePath);

        Drawable placeholder = VectorDrawableCompat
                .create(context.getResources(),
                        R.drawable.ic_nature_people_white_48px, context.getTheme());


        Picasso.get()
                .load(imagePath)
                .placeholder(placeholder)
                .into(marketPhoto);

    }









    @OnClick(R.id.list_item)
    void listItemClick()
    {
        subscriber.listItemClick(configurationGlobal,getLayoutPosition());
    }





    @OnClick(R.id.select_market)
    void selectMarket()
    {


        ServiceConfigurationGlobal configurationGlobal = this.configurationGlobal;


        if(PrefLoginGlobal.getUser(context)==null)
        {
            // user not logged in so just fetch configuration
            fetchConfiguration(configurationGlobal);
        }
        else
        {
            // user logged in so make an attempt to login to local service
            loginToLocalEndpoint(configurationGlobal);
        }
    }





    void fetchConfiguration(final ServiceConfigurationGlobal configurationGlobal)
    {

//            PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),getApplicationContext());
//            PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());



//            PrefGeneral.getServiceURL(MyApplication.getAppContext())


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(configurationGlobal.getServiceURL())
                .client(new OkHttpClient().newBuilder().build())
                .build();




        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(0.0,0.0);




        selectMarket.setVisibility(View.INVISIBLE);
        progressBarSelect.setVisibility(View.VISIBLE);


        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {


                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);




                if(response.code()==200)
                {

                    PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),context);
                    PrefServiceConfig.saveServiceConfigLocal(response.body(),context);


                    ServiceConfigurationLocal config = response.body();

                    if(config!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",config.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),context);
                    }



                    subscriber.selectMarketSuccessful(configurationGlobal,getLayoutPosition());
                }
                else
                {
//                        PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
//                        PrefGeneral.saveServiceURL(null,getApplicationContext());


                    showToastMessage("Failed Code : " + String.valueOf(response.code()));
                }


            }



            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {


                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);
                showToastMessage("Failed ... Please check your network ! ");


            }
        });
    }




    void loginToLocalEndpoint(final ServiceConfigurationGlobal configurationGlobal)
    {

//        final String phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();




        selectMarket.setVisibility(View.INVISIBLE);
        progressBarSelect.setVisibility(View.VISIBLE);



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(configurationGlobal.getServiceURL())
                .client(new OkHttpClient().newBuilder().build())
                .build();





        Call<User> call = retrofit.create(LoginUsingOTPService.class).loginWithGlobalCredentials(
                PrefLoginGlobal.getAuthorizationHeaders(context),
                PrefServiceConfig.getServiceURL_SDS(context),
                123,true,false
        );






        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {




                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);



                if(response.code()==200)
                {
                    // save username and password




                    PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),context);



                    User user = response.body();



                    String username = "";

                    if(user.getPhone()!=null)
                    {
                        username = user.getPhone();
                    }
                    else if(user.getEmail()!=null)
                    {
                        username = user.getEmail();
                    }
                    else if(user.getUsername()!=null)
                    {
                        username = user.getUsername();
                    }
                    else if(user.getUserID()!=0)
                    {
                        username = String.valueOf(user.getUserID());
                    }


                    // local username can be different from the supplied username


                    PrefLogin.saveCredentials(
                            context,
                            username,
                            user.getPassword()
                    );


//                    PrefLogin.saveCredentials(
//                            context,
//                            PrefLoginGlobal.getUsername(context),
//                            PrefLoginGlobal.getPassword(context)
//                    );






                    PrefLogin.saveUserProfile(
                            user,
                            context
                    );



                    ServiceConfigurationLocal configurationLocal = user.getServiceConfigurationLocal();

                    PrefServiceConfig.saveServiceConfigLocal(configurationLocal,context);



                    if(configurationLocal!=null)
                    {
                        Currency currency = Currency.getInstance(new Locale("",configurationLocal.getISOCountryCode()));
                        PrefGeneral.saveCurrencySymbol(currency.getSymbol(),context);
                    }




                    subscriber.selectMarketSuccessful(configurationGlobal,getLayoutPosition());

                }
                else
                {
                    subscriber.showMessage("Login Failed : Username or password is incorrect !");
                    System.out.println("Login Failed : Code " + String.valueOf(response.code()));
                }

            }




            @Override
            public void onFailure(Call<User> call, Throwable t) {



                subscriber.showMessage("Failed ... Please check your network connection !");

                selectMarket.setVisibility(View.VISIBLE);
                progressBarSelect.setVisibility(View.INVISIBLE);


            }
        });
    }






    void showToastMessage(String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }


}

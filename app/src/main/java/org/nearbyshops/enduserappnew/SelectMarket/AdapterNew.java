package org.nearbyshops.enduserappnew.SelectMarket;

import android.graphics.drawable.Drawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.Login.NotifyAboutLogin;
import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationGlobal;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;
import org.nearbyshops.enduserappnew.Preferences.PrefLoginGlobal;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.LoginUsingOTPService;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ServiceConfigurationService;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

/**
 * Created by sumeet on 13/6/16.
 */
public class AdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ServiceConfigurationGlobal> dataset = null;
    private NoticationsFromServiceAdapter notications;


    public static final int VIEW_TYPE_SERVICE = 1;
    public static final int VIEW_TYPE_SCROLL_PROGRESS_BAR = 2;


    @Inject Gson gson;



    private Fragment fragment;






    AdapterNew(List<ServiceConfigurationGlobal> dataset, NoticationsFromServiceAdapter notificationsFromAdapter, Fragment fragment) {
        this.dataset = dataset;
        this.notications = notificationsFromAdapter;
        this.fragment = fragment;



        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        if(viewType== VIEW_TYPE_SERVICE)
        {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_market,parent,false);

            return new ViewHolder(view);
        }
        else if(viewType==VIEW_TYPE_SCROLL_PROGRESS_BAR)
        {

            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_progress_bar,parent,false);

            return new LoadingViewHolder(view);
        }

        return null;


    }


    @Override
    public int getItemViewType(int position) {

        super.getItemViewType(position);

        if(position == dataset.size())
        {
            return VIEW_TYPE_SCROLL_PROGRESS_BAR;
        }
        else
        {
            return VIEW_TYPE_SERVICE;
        }

//        return -1;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holderVH, int position) {

        if(holderVH instanceof ViewHolder)
        {
            ViewHolder holder = (ViewHolder) holderVH;

            if(dataset!=null)
            {
                ServiceConfigurationGlobal service = dataset.get(position);

                holder.serviceName.setText(service.getServiceName());
//                holder.serviceURL.setText(service.getServiceURL());
                holder.serviceAddress.setText(service.getCity());

//                service.getAddress() + ", " +



//                if(service.getVerified())
//                {
//                    holder.indicatorVerified.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    holder.indicatorVerified.setVisibility(View.GONE);
//                }



//                holder.indicatorVerified.setVisibility(View.VISIBLE);



                holder.distance.setText("Distance : " + String.format("%.2f",service.getRt_distance()));
//                holder.rating.setText(String.format("%.2f",));



                holder.description.setText(service.getDescriptionShort());








                String imagePath = PrefServiceConfig.getServiceURL_SDS(fragment.getActivity())
                        + "/api/v1/ServiceConfiguration/Image/three_hundred_" + service.getLogoImagePath() + ".jpg";

//                System.out.println("Service LOGO : " + imagePath);

                Drawable placeholder = VectorDrawableCompat
                        .create(fragment.getActivity().getResources(),
                                R.drawable.ic_nature_people_white_48px, fragment.getActivity().getTheme());



                Picasso.with(fragment.getActivity())
                        .load(imagePath)
                        .placeholder(placeholder)
                        .into(holder.serviceLogo);


            }
        }

        else if(holderVH instanceof LoadingViewHolder)
        {


            LoadingViewHolder viewHolder = (LoadingViewHolder) holderVH;

            if(fragment instanceof ServicesFragment)
            {
                int items_count = ((ServicesFragment) fragment).item_count;

                if(dataset.size() == items_count)
                {
                    viewHolder.progressBar.setVisibility(View.GONE);
                }
                else
                {
                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setIndeterminate(true);

                }
            }
        }
    }





    @Override
    public int getItemCount() {
        return (dataset.size()+1);
    }


    public class LoadingViewHolder extends  RecyclerView.ViewHolder{

        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }







    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



        @BindView(R.id.service_name) TextView serviceName;
        @BindView(R.id.address) TextView serviceAddress;
        @BindView(R.id.indicator_category) TextView indicatorCategory;
        @BindView(R.id.indicator_verified) TextView indicatorVerified;
        @BindView(R.id.distance) TextView distance;
        @BindView(R.id.rating) TextView rating;
        @BindView(R.id.rating_count) TextView ratingCount;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.logo) ImageView serviceLogo;

        @BindView(R.id.progress_bar_select) ProgressBar progressBarSelect;
        @BindView(R.id.select_market) TextView selectMarket;



        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }



//        @OnClick(R.id.description)
//        void copyURLClick()
//        {
//            ClipboardManager clipboard = (ClipboardManager) fragment.getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clip = ClipData.newPlainText("URL", serviceURL.getText().toString());
//            clipboard.setPrimaryClip(clip);
//
//            showToastMessage("Copied !");
//        }








        @OnClick(R.id.select_market)
        void selectMarket()
        {


            ServiceConfigurationGlobal configurationGlobal = dataset.get(getLayoutPosition());


            if(PrefLoginGlobal.getUser(getApplicationContext())==null)
            {
                // user not logged in so just fetch configuration

                fetchConfiguration(configurationGlobal);
            }
            else
            {
                // user logged in so make an attempt to login to local service

                loginToLocalEndpoint();
            }


        }



        @Override
        public void onClick(View v) {
            notications.notifyListItemClick(dataset.get(getLayoutPosition()));
        }





        void fetchConfiguration(ServiceConfigurationGlobal configurationGlobal)
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

                        PrefGeneral.saveServiceURL(configurationGlobal.getServiceURL(),getApplicationContext());
                        PrefServiceConfig.saveServiceConfigLocal(response.body(),getApplicationContext());


                        ServiceConfigurationLocal config = response.body();

                        if(config!=null)
                        {
                            Currency currency = Currency.getInstance(new Locale("",config.getISOCountryCode()));
                            PrefGeneral.saveCurrencySymbol(currency.getSymbol(),getApplicationContext());
                        }


                        notications.selectMarketClick(dataset.get(getLayoutPosition()));
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

//                    PrefServiceConfig.saveServiceConfigLocal(null,getApplicationContext());
                }
            });
        }




        void loginToLocalEndpoint()
        {

//        final String phoneWithCode = ccp.getSelectedCountryCode()+ username.getText().toString();




            selectMarket.setVisibility(View.INVISIBLE);
            progressBarSelect.setVisibility(View.VISIBLE);



            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                    .client(new OkHttpClient().newBuilder().build())
                    .build();




            Call<User> call = retrofit.create(LoginUsingOTPService.class).loginWithGlobalCredentials(
                    PrefLoginGlobal.getAuthorizationHeaders(getApplicationContext()),
                    PrefServiceConfig.getServiceURL_SDS(getApplicationContext()),
                    123
            );





            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {




                    selectMarket.setVisibility(View.VISIBLE);
                    progressBarSelect.setVisibility(View.INVISIBLE);



                    if(response.code()==200)
                    {
                        // save username and password





                        User user = response.body();

                        PrefLogin.saveCredentials(
                                getApplicationContext(),
                                user.getPhone(),
                                user.getPassword()
                        );





                        // save user profile information
                        PrefLogin.saveUserProfile(
                                response.body(),
                                getApplicationContext()
                        );





//                        if(PrefLoginGlobal.getUser(getApplicationContext())==null)
//                        {
//                            PrefLoginGlobal.saveUserProfile(
//                                    response.body(),
//                                    getApplicationContext()
//                            );
//                        }







//                    PrefOneSignal.saveToken(getActivity(),PrefOneSignal.getLastToken(getActivity()));
//
//                    if(PrefOneSignal.getToken(getActivity())!=null)
//                    {
//                        // update one signal id if its not updated
//                        getActivity().startService(new Intent(getActivity(), UpdateOneSignalID.class));
//                    }









//
//                        if( instanceof NotifyAboutLogin)
//                        {
////                        showToastMessage("Notify about login !");
//                            ((NotifyAboutLogin) getActivity()).loginSuccess();
//                        }



                        notications.selectMarketClick(dataset.get(getLayoutPosition()));

//                        getActivity().finish();


//                    showToastMessage("LoginUsingOTP success : code : " + String.valueOf(response.code()));



                    }
                    else
                    {
                        showToastMessage("Login Failed : Username or password is incorrect !");
                        System.out.println("Login Failed : Code " + String.valueOf(response.code()));
                    }

                }




                @Override
                public void onFailure(Call<User> call, Throwable t) {



                    showToastMessage("Failed ... Please check your network connection !");

                    selectMarket.setVisibility(View.VISIBLE);
                    progressBarSelect.setVisibility(View.INVISIBLE);


                }
            });
        }




    }






    void showToastMessage(String message)
    {
        if(fragment.getActivity()!=null)
        {
            Toast.makeText(fragment.getActivity(),message,Toast.LENGTH_SHORT).show();
        }

    }





    interface NoticationsFromServiceAdapter{
        void notifyListItemClick(ServiceConfigurationGlobal serviceConfigurationGlobal);
        void selectMarketClick(ServiceConfigurationGlobal serviceConfigurationGlobal);
    }

}

package org.nearbyshops.enduser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.apache.commons.validator.routines.UrlValidator;
import org.nearbyshops.enduser.Model.EndUser;
import org.nearbyshops.enduser.Model.Service;
import org.nearbyshops.enduser.RetrofitRESTContract.EndUserService;
import org.nearbyshops.enduser.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.enduser.Utility.UtilityGeneral;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity implements View.OnClickListener, Target{

    @Bind(R.id.serviceURLEditText)
    EditText serviceUrlEditText;

    @Bind(R.id.distributorIDEdittext)
    EditText distributorIDEditText;

    @Bind(R.id.distributorPassword)
    EditText password;

    @Bind(R.id.loginButton)
    Button loginButton;

    @Bind(R.id.signUpButton)
    Button signUpButton;


    @Bind(R.id.inputLayoutServiceURL)
    TextInputLayout inputLayoutServiceURL;


    @Bind(R.id.urlValidText)
    TextView urlValidText;


    @Bind(R.id.login_backdrop)
    ImageView loginBackdrop;

    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.serviceURLBar)
    LinearLayout serviceBar;


    // location services

    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;


//    ServiceConfigurationService configurationService;

    Toolbar toolbar;

    UrlValidator urlValidator = null;

    public Login() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent()
                .Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        serviceUrlEditText.setText(UtilityGeneral.getServiceURL(MyApplication.getAppContext()));
        //distributorIDEditText.setText(String.valueOf(UtilityGeneral.getEndUserID(MyApplication.getAppContext())));

        //makeServiceConfigCall();

        String[] schemes = {"http","https"};

        urlValidator = new UrlValidator(schemes);



        makeServiceConfigCall();


        serviceUrlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //UtilityGeneral.saveServiceURL(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                //URLUtil.isValidUrl(s.toString())

                if(urlValidator.isValid(s.toString()))
                {
                    UtilityGeneral.saveServiceURL(s.toString());



                    //inputLayoutServiceURL.setErrorEnabled(false);

                    //inputLayoutServiceURL.setError(null);
                    //inputLayoutServiceURL.setErrorEnabled(false);
                    urlValidText.setVisibility(View.GONE);

                    makeServiceConfigCall();

                }else
                {
                    //showToastMessage("Invalid URL");

                    //inputLayoutServiceURL.setErrorEnabled(true);
                    //inputLayoutServiceURL.setError("Invalid URL");

                    //Log.d("applog","Invalid URL");

                    urlValidText.setVisibility(View.VISIBLE);
                    urlValidText.setText("Invalid URL: Please Enter a valid URL");

                }

            }
        });




            /*

        distributorIDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals(new String(""))) {

                    UtilityGeneral.saveEndUserID(Integer.parseInt(s.toString()));
                }
            }
        });

            */


    }




    void makeServiceConfigCall()
    {


        if(!urlValidator.isValid(UtilityGeneral.getServiceURL(MyApplication.getAppContext())))
        {
            return;
        }



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();


        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);


        Call<Service> call = service.getService(1);

        call.enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {

                if(response.body()!=null)
                {
                    Service service = response.body();

                    loadImage(service.getImagePath());
                }
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {

//                showToastMessage("Network request failed !");

            }
        });
    }



    void loadImage(String serviceImagePath)
    {
        String imagePath = UtilityGeneral.getConfigImageEndpointURL(MyApplication.getAppContext())
                + serviceImagePath;

        Picasso.with(this)
                .load(imagePath)
                .placeholder(R.drawable.images)
                .into(loginBackdrop);


        Picasso.with(this)
                .load(imagePath)
                .placeholder(R.drawable.images)
                .into(this);

    }



    // obtaining colors dynamically from the backdrop image poster
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        //actionBarImage.setImageBitmap(bitmap);

        Palette palette = Palette.from(bitmap).generate();

        int color = 323235;
        int vibrant = palette.getVibrantColor(color);
        int vibrantLight = palette.getLightVibrantColor(color);
        int vibrantDark = palette.getDarkVibrantColor(color);
        int muted = palette.getMutedColor(color);
        int mutedLight = palette.getLightMutedColor(color);
        int mutedDark = palette.getDarkMutedColor(color);

        Palette.Swatch vibrantSwatch = palette.getVibrantSwatch();

        //if(vibrantSwatch!=null) {
        //  originalTitle.setTextColor(vibrantSwatch.getTitleTextColor());
        //}




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(vibrantDark);


        }

        inputLayoutServiceURL.setBackgroundColor(vibrantDark);
        serviceBar.setBackgroundColor(muted);

//        if(fab!=null && vibrantDark!=0) {
//
//            fab.setBackgroundTintList(ColorStateList.valueOf(vibrantDark));
//
//        }//fab.setBackgroundColor(vibrantDark);

        //originalTitle.setBackgroundColor(vibrantDark);


        if(collapsingToolbarLayout!=null) {

            collapsingToolbarLayout.setContentScrimColor(vibrant);

        }
        //actionBarImage.setImageBitmap(bitmap);

//        movieDetailAdapter.notifyColorChange(vibrantDark,muted);

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }




    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.loginButton:

                startActivity(new Intent(this,Home.class));

                break;

        }

    }


    @OnClick(R.id.loginButton)
    public void login()
    {
        makeLoginRequest();
    }



    void makeLoginRequest()
    {


        if(!urlValidator.isValid(UtilityGeneral.getServiceURL(MyApplication.getAppContext())))
        {
            return;
        }



        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UtilityGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();


        EndUserService service = retrofit.create(EndUserService.class);


        String username = distributorIDEditText.getText().toString();
        String passwordstr = password.getText().toString();




        if(!username.equals("")&&!passwordstr.equals(""))
        {

            Call<EndUser> call = service.getEndUser(passwordstr,username);



            call.enqueue(new Callback<EndUser>() {
                @Override
                public void onResponse(Call<EndUser> call, Response<EndUser> response) {


                    if(response.code()==200)
                    {
                        showToastMessage("Login Successful");


                        if(response.body()!= null)
                        {
                            UtilityGeneral.saveEndUserID(response.body().getEndUserID());

//                            showToastMessage(response.body().getEndUserName() + " : " + response.body().getEndUserID());

                            startActivity(new Intent(Login.this,Home.class));
                        }


                    }
                    else if(response.code()==401)
                    {
                        showToastMessage("Username or password is incorrect");
                    }
                    else
                    {
                        showToastMessage("Server Error !");
                    }



                }

                @Override
                public void onFailure(Call<EndUser> call, Throwable t) {

                    showToastMessage("Network request failed !");
                }
            });

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

}

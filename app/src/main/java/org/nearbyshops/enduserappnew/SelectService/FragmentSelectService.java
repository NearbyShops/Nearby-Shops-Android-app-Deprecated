package org.nearbyshops.enduserappnew.SelectService;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import org.apache.commons.validator.routines.UrlValidator;
import org.nearbyshops.enduserappnew.ModelServiceConfig.ServiceConfigurationLocal;
import org.nearbyshops.enduserappnew.MyApplication;
import org.nearbyshops.enduserappnew.Preferences.PrefGeneral;
import org.nearbyshops.enduserappnew.Preferences.PrefLocation;
import org.nearbyshops.enduserappnew.Preferences.PrefServiceConfig;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.ServiceConfigurationService;
import org.nearbyshops.enduserappnew.Markets.DeprecatedCode.SlidingLayerSort.ServicesActivity;

import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sumeet on 15/1/17.
 */

public class FragmentSelectService extends Fragment {


    @BindView(R.id.serviceURL) EditText serviceURL;
    @BindView(R.id.text_input_service_url) TextInputLayout textInputServiceURL;

    UrlValidator urlValidator;



//    @Bind(R.id.service_url) TextInputEditText service_url;
    @BindView(R.id.reset_button) TextView resetButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        setRetainInstance(true);

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_select_service, container, false);
        ButterKnife.bind(this,rootView);


//        bindDataToViews();


        String[] schemes = {"http", "https"};

        urlValidator = new UrlValidator(schemes);

        serviceURL.setText(PrefGeneral.getServiceURL(getActivity()));

        serviceURL.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (urlValidator.isValid(s.toString())) {
                    PrefGeneral.saveServiceURL(s.toString(),getActivity());
                    textInputServiceURL.setError(null);
                    textInputServiceURL.setErrorEnabled(false);
                    updateStatusLight();
                }
                else
                {
//                    serviceURL.setError("URL Invalid");
                    textInputServiceURL.setErrorEnabled(true);
                    textInputServiceURL.setError("Invalid URL");

                    PrefGeneral.saveServiceLightStatus(getActivity(),STATUS_LIGHT_RED);
                    setStatusLight();

                }

            }
        });


        setStatusLight();


        return rootView;
    }






    @OnClick(R.id.reset_button)
    void resetButtonClick()
    {
        PrefGeneral.saveServiceURL("http://nearbyshops.org",getActivity());
//        bindDataToViews();
    }


    boolean isDestroyed = false;

    @Override
    public void onResume() {
        super.onResume();
        isDestroyed = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }



    void updateStatusLight()
    {

        GsonBuilder gsonBuilder = new GsonBuilder();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
                .baseUrl(PrefGeneral.getServiceURL(MyApplication.getAppContext()))
                .build();

        ServiceConfigurationService service = retrofit.create(ServiceConfigurationService.class);

        Call<ServiceConfigurationLocal> call = service.getServiceConfiguration(
                PrefLocation.getLatitude(getActivity()),
                PrefLocation.getLongitude(getActivity())
        );


        call.enqueue(new Callback<ServiceConfigurationLocal>() {
            @Override
            public void onResponse(Call<ServiceConfigurationLocal> call, Response<ServiceConfigurationLocal> response) {

                if(isDestroyed)
                {
                    return;
                }

                if(response.code()==200)
                {
                    if(response.body()!=null)
                    {
                        ServiceConfigurationLocal configurationLocal = response.body();
                        PrefServiceConfig.saveServiceConfigLocal(configurationLocal,getActivity());
                        saveCurrency(configurationLocal.getISOCountryCode(), configurationLocal.getISOLanguageCode());


//                        if(UtilityLocation.getLongitude(Home.this)!=null && UtilityLocation.getLatitude(Home.this)!=null)
//                        {

//                            Location locationUser = new Location("user");
//                            locationUser.setLongitude(UtilityLocation.getLongitude(Home.this));
//                            locationUser.setLatitude(UtilityLocation.getLatitude(Home.this));
//
//                            Location locationProvider = new Location("provider");
//                            locationProvider.setLatitude(configurationLocal.getLatCenter());
//                            locationProvider.setLongitude(configurationLocal.getLonCenter());


//                            float distance  = locationProvider.distanceTo(locationUser);


                        if(configurationLocal.getRt_distance()<=configurationLocal.getServiceRange())
                        {
                            PrefGeneral.saveServiceLightStatus(getActivity(),STATUS_LIGHT_GREEN);
                            setStatusLight();
                        }
                        else
                        {
                            PrefGeneral.saveServiceLightStatus(getActivity(),STATUS_LIGHT_YELLOW);
                            setStatusLight();
                        }
//                        }
//                        else
//                        {
//                            UtilityGeneral.saveServiceLightStatus(Home.this,STATUS_LIGHT_YELLOW);
//                            setStatusLight();
//                        }


                    }
                    else
                    {
                        PrefGeneral.saveServiceLightStatus(getActivity(),STATUS_LIGHT_RED);
                        setStatusLight();
                    }
                }
                else
                {
                    PrefGeneral.saveServiceLightStatus(getActivity(),STATUS_LIGHT_RED);
                    setStatusLight();
                }
            }

            @Override
            public void onFailure(Call<ServiceConfigurationLocal> call, Throwable t) {

                if(isDestroyed)
                {
                    return;
                }


                PrefGeneral.saveServiceLightStatus(getActivity(),STATUS_LIGHT_RED);
                setStatusLight();

            }
        });

    }


    @BindView(R.id.status_indicator_one) TextView statusLight;
    public static final int STATUS_LIGHT_GREEN = 1;
    public static final int STATUS_LIGHT_YELLOW = 2;
    public static final int STATUS_LIGHT_RED = 3;


    void setStatusLight()
    {

        int status = PrefGeneral.getServiceLightStatus(getActivity());

        if(status == STATUS_LIGHT_GREEN)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gplus_color_1));
        }
        else if(status == STATUS_LIGHT_YELLOW)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.gplus_color_2));
        }
        else if(status == STATUS_LIGHT_RED)
        {
            statusLight.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.deepOrange900));
        }

    }




    @OnClick(R.id.discover_services_button)
    void discoverServicesClick()
    {
        startActivity(new Intent(getActivity(), ServicesActivity.class));
    }


    @OnClick(R.id.paste_url_button)
    void pasteURLClick()
    {
        ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

        if(clipboard.getPrimaryClip()!=null)
        {
            serviceURL.setText(clipboard.getPrimaryClip().getItemAt(0).getText());
        }
    }






    void saveCurrency(String countryCode, String languageCode)
    {
        try {
            Locale locale = new Locale(languageCode,countryCode);
            Currency currency = Currency.getInstance(locale);
            PrefGeneral.saveCurrencySymbol(currency.getSymbol(),getActivity());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }




}

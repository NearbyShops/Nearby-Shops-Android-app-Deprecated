package org.nearbyshops.enduserappnew.DeliveryAddress.EditAddress;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
//import org.nearbyshops.enduserappnew.DeliveryAddress.PickLocationActivity;
import org.nearbyshops.enduserappnew.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.DeliveryAddressService;
import org.nearbyshops.enduserappnew.Preferences.PrefLogin;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class EditAddressFragment extends Fragment{

    DeliveryAddress deliveryAddress;

    public static final String DELIVERY_ADDRESS_INTENT_KEY = "edit_delivery_address_intent_key";

    @Inject DeliveryAddressService deliveryAddressService;

    @BindView(R.id.updateAddress) TextView updateDeliveryAddress;
    // address Fields
    @BindView(R.id.receiversName) EditText receiversName;
    @BindView(R.id.receiversPhoneNumber) EditText receiversPhoneNumber;
    @BindView(R.id.deliveryAddress) EditText deliveryAddressView;
    @BindView(R.id.addressCity) EditText city;
    @BindView(R.id.pincode) EditText pincode;
    @BindView(R.id.landmark) EditText landMark;
    @BindView(R.id.latitude) EditText latitude;
    @BindView(R.id.longitude) EditText longitude;



    public static final String EDIT_MODE_INTENT_KEY = "edit_mode";

    public static final int MODE_UPDATE = 52;
    public static final int MODE_ADD = 51;

    int current_mode = MODE_ADD;



    public EditAddressFragment() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.content_edit_address, container, false);
        ButterKnife.bind(this,rootView);

//        setContentView(R.layout.activity_edit_address);


        current_mode = getActivity().getIntent().getIntExtra(EDIT_MODE_INTENT_KEY,MODE_ADD);

        if(current_mode ==MODE_UPDATE)
        {
            deliveryAddress = getActivity().getIntent().getParcelableExtra(DELIVERY_ADDRESS_INTENT_KEY);
            bindDataToViews();
        }


        return rootView;
    }




    void getDataFromViews()
    {
        if(deliveryAddress!=null)
        {
            deliveryAddress.setName(receiversName.getText().toString());
            deliveryAddress.setDeliveryAddress(deliveryAddressView.getText().toString());
            deliveryAddress.setCity(city.getText().toString());
            deliveryAddress.setPincode(Long.parseLong(pincode.getText().toString()));
            deliveryAddress.setLandmark(landMark.getText().toString());
            deliveryAddress.setPhoneNumber(Long.parseLong(receiversPhoneNumber.getText().toString()));


            deliveryAddress.setLatitude(Double.parseDouble(latitude.getText().toString()));
            deliveryAddress.setLongitude(Double.parseDouble(longitude.getText().toString()));
        }
    }


    void bindDataToViews()
    {
        if(deliveryAddress!=null)
        {
            receiversName.setText(deliveryAddress.getName());
            deliveryAddressView.setText(deliveryAddress.getDeliveryAddress());
            city.setText(deliveryAddress.getCity());
            pincode.setText(String.valueOf(deliveryAddress.getPincode()));
            landMark.setText(deliveryAddress.getLandmark());
            receiversPhoneNumber.setText(String.valueOf(deliveryAddress.getPhoneNumber()));

            latitude.setText(String.valueOf(deliveryAddress.getLatitude()));
            longitude.setText(String.valueOf(deliveryAddress.getLongitude()));

        }
    }



    @OnClick(R.id.updateAddress)
    void updateAddressClick(View view)
    {

        if(!validateData())
        {
            return;
        }

        if(current_mode == MODE_ADD)
        {
            addDeliveryAddress();
        }
        else if(current_mode == MODE_UPDATE)
        {
            updateAddress();
        }

    }



    boolean validateData()
    {
        boolean isValid = true;



        if(longitude.getText().toString().length()==0)
        {
            longitude.setError("Longitude cant be empty !");
            longitude.requestFocus();
            isValid= false;
        }
        else
        {
            double lon = Double.parseDouble(longitude.getText().toString());

            if(lon >180 || lon < -180)
            {
                longitude.setError("Invalid Longitude !");
                isValid = false;
            }

        }


        if(latitude.getText().toString().length()==0)
        {
            latitude.setError("Latitude cant be empty !");
            latitude.requestFocus();
            isValid = false;
        }
        else
        {
            double lat = Double.parseDouble(latitude.getText().toString());

            if(lat >90 || lat <- 90)
            {
                latitude.setError("Invalid Latitude !");
                isValid  = false;
            }
        }




        if(pincode.getText().toString().length()==0)
        {
            pincode.setError("Pincode cannot be empty !");
            pincode.requestFocus();
            isValid = false;
        }

        if(receiversPhoneNumber.getText().toString().length()==0)
        {
            receiversPhoneNumber.setError("Phone number cannot be empty !");
            receiversPhoneNumber.requestFocus();
            isValid = false;
        }


        return isValid;
    }




    void addDeliveryAddress()
    {
        if(PrefLogin.getUser(getActivity())==null)
        {
            showToastMessage("Please login to use this feature !");
            return;
        }


        if(deliveryAddress==null)
        {
            deliveryAddress = new DeliveryAddress();
        }

        getDataFromViews();
        deliveryAddress.setEndUserID(PrefLogin.getUser(getActivity()).getUserID());

        Call<DeliveryAddress> call = deliveryAddressService.postAddress(deliveryAddress);
        call.enqueue(new Callback<DeliveryAddress>() {
            @Override
            public void onResponse(Call<DeliveryAddress> call, Response<DeliveryAddress> response) {

                if (response != null && response.code() == 201) {

                    showToastMessage("Added Successfully !");

                    current_mode = MODE_UPDATE;
//                    updateIDFieldVisibility();
                    deliveryAddress = response.body();
//                    bindDataToViews();

                }
                else
                {
                    showToastMessage("Unsuccessful !");
                }
            }

            @Override
            public void onFailure(Call<DeliveryAddress> call, Throwable t) {

                showToastMessage("Network Connection Failed !");
            }
        });


    }

    void updateAddress()
    {
        getDataFromViews();

        Call<ResponseBody> call = deliveryAddressService.putAddress(deliveryAddress,deliveryAddress.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code()==200)
                {
                    showToastMessage("Update Successful !");
                }
                else
                {
                    showToastMessage("failed to update !");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                showToastMessage("Network connection failed !");
            }
        });
    }


    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
    }


    private int REQUEST_CODE_PICK_LAT_LON = 23;

    @OnClick(R.id.pick_location_button)
    void pickLocationClick()
    {
//        Intent intent = new Intent(getActivity(),PickLocationActivity.class);
//        startActivityForResult(intent,REQUEST_CODE_PICK_LAT_LON);
    }


    @OnClick(R.id.navigate_button)
    void navigateButton()
    {
        String str_latitude = latitude.getText().toString();
        String str_longitude = longitude.getText().toString();

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + str_latitude +  "," + str_longitude);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_LAT_LON)
        {
            latitude.setText(String.valueOf(data.getDoubleExtra("latitude",0)));
            longitude.setText(String.valueOf(data.getDoubleExtra("longitude",0)));
        }
    }




}

package org.nearbyshops.enduserapp.DeliveryAddress.Previous;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduserapp.DaggerComponentBuilder;
import org.nearbyshops.enduserapp.DeliveryAddress.PickLocationActivity;
import org.nearbyshops.enduserapp.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.RetrofitRESTContract.DeliveryAddressService;
import org.nearbyshops.enduserapp.Utility.PrefLogin;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity implements View.OnClickListener, Callback<DeliveryAddress> {

    DeliveryAddress deliveryAddress;

    @Inject
    DeliveryAddressService deliveryAddressService;

    TextView addDeliveryAddress;

    // address Fields

    @BindView(R.id.receiversName)
    EditText receiversName;

    @BindView(R.id.receiversPhoneNumber)
    EditText receiversPhoneNumber;

    @BindView(R.id.deliveryAddress)
    EditText deliveryAddressView;

    @BindView(R.id.addressCity)
    EditText city;

    @BindView(R.id.pincode)
    EditText pincode;

    @BindView(R.id.landmark)
    EditText landMark;

    @BindView(R.id.latitude)
    EditText latitude;

    @BindView(R.id.longitude)
    EditText longitude;



    public AddAddressActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Bind Views

        addDeliveryAddress = (TextView) findViewById(R.id.addDeliveryAddress);
        addDeliveryAddress.setOnClickListener(this);

    }



    void getDataFromViews()
    {

        deliveryAddress = new DeliveryAddress();

        deliveryAddress.setName(receiversName.getText().toString());
        deliveryAddress.setDeliveryAddress(deliveryAddressView.getText().toString());
        deliveryAddress.setCity(city.getText().toString());

        if(pincode.getText().toString()!="")
        {
            deliveryAddress.setPincode(Long.parseLong(pincode.getText().toString()));
        }

        deliveryAddress.setLandmark(landMark.getText().toString());
        deliveryAddress.setPhoneNumber(Long.parseLong(receiversPhoneNumber.getText().toString()));

        deliveryAddress.setLatitude(Double.parseDouble(latitude.getText().toString()));
        deliveryAddress.setLongitude(Double.parseDouble(longitude.getText().toString()));

        deliveryAddress.setEndUserID(PrefLogin.getUser(this).getUserID());

    }


    @Override
    public void onClick(View v) {

        getDataFromViews();

        if(deliveryAddress!=null)
        {
            Call<DeliveryAddress> call = deliveryAddressService.postAddress(deliveryAddress);
            call.enqueue(this);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        ButterKnife.unbind(this);
    }

    @Override
    public void onResponse(Call<DeliveryAddress> call, Response<DeliveryAddress> response) {

        if (response != null && response.code() == 201) {
            showToastMessage("Added Successfully !");
        }
        else
        {
            showToastMessage("Unsuccessful !");
        }
    }

    @Override
    public void onFailure(Call<DeliveryAddress> call, Throwable t) {

        showToastMessage("Addition failed. Try again !");
    }



    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



    private int REQUEST_CODE_PICK_LAT_LON = 23;

    @OnClick(R.id.pick_location_button)
    void pickLocationClick()
    {
        Intent intent = new Intent(this,PickLocationActivity.class);
        startActivityForResult(intent,REQUEST_CODE_PICK_LAT_LON);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_LAT_LON)
        {
            latitude.setText(String.valueOf(data.getDoubleExtra("latitude",0)));
            longitude.setText(String.valueOf(data.getDoubleExtra("longitude",0)));
        }
    }



}

package org.nearbyshops.enduserappnew.DeliveryAddress.Previous;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.nearbyshops.enduserappnew.DaggerComponentBuilder;
import org.nearbyshops.enduserappnew.ModelStats.DeliveryAddress;
import org.nearbyshops.enduserappnew.R;
import org.nearbyshops.enduserappnew.RetrofitRESTContract.DeliveryAddressService;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAddressActivity extends AppCompatActivity implements Callback<ResponseBody> {

    DeliveryAddress deliveryAddress;

    public static final String DELIVERY_ADDRESS_INTENT_KEY = "edit_delivery_address_intent_key";


    @Inject
    DeliveryAddressService deliveryAddressService;

    @BindView(R.id.updateAddress)
    TextView updateDeliveryAddress;

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


    public EditAddressActivity() {

        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        deliveryAddress = getIntent().getParcelableExtra(DELIVERY_ADDRESS_INTENT_KEY);

        bindDataToViews();

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

        getDataFromViews();

        Call<ResponseBody> call = deliveryAddressService.putAddress(deliveryAddress,deliveryAddress.getId());
        call.enqueue(this);

    }


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

    void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

//        ButterKnife.unbind(this);
    }




    private int REQUEST_CODE_PICK_LAT_LON = 23;

    @OnClick(R.id.pick_location_button)
    void pickLocationClick()
    {
//        Intent intent = new Intent(this,PickLocationActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK_LAT_LON)
        {
            latitude.setText(String.valueOf(data.getDoubleExtra("latitude",0)));
            longitude.setText(String.valueOf(data.getDoubleExtra("longitude",0)));
        }
    }




}

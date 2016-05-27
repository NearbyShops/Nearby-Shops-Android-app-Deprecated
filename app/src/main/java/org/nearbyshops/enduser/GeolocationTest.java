package org.nearbyshops.enduser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.nearbyshops.enduser.R;
import org.nearbyshops.enduser.Utility.GeoLocation;

import butterknife.Bind;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class GeolocationTest extends AppCompatActivity {

    @Bind(R.id.latmax) TextView latMax;

    @Bind(R.id.latmin) TextView latMin;

    @Bind(R.id.lonmin) TextView lonMin;

    @Bind(R.id.lonmax) TextView lonMax;

//    private Unbinder unbinder;

    GeoLocation center;

    GeoLocation[] minMaxArray;
    GeoLocation pointOne;
    GeoLocation pointTwo;

    @Bind(R.id.buttonCalculate) Button calculate;

    @Bind(R.id.enterLatitude) EditText enterLatitude;
    @Bind(R.id.enterLontitude) EditText enterLongitude;
    @Bind(R.id.enterDistance) EditText enterDistance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


    }

    @OnClick(R.id.buttonCalculate)
    public void boundingCoordinates()
    {
        double centerLongitude = Double.parseDouble(enterLongitude.getText().toString());
        double centerLatitude = Double.parseDouble(enterLatitude.getText().toString());
        double distance = Double.parseDouble(enterDistance.getText().toString());

        center = GeoLocation.fromDegrees(centerLatitude,centerLongitude);
        minMaxArray = center.boundingCoordinates(distance,6371.01);

        pointOne = minMaxArray[0];
        pointTwo = minMaxArray[1];

        latMax.setText("P1: Latitude Max : " + pointOne.getLatitudeInDegrees());
        lonMin.setText("P1: Longitude Min : " + pointOne.getLongitudeInDegrees());

        latMin.setText("P2: Latitude Min : " + pointTwo.getLatitudeInDegrees());
        lonMax.setText("P2: Longitude Max : " + pointTwo.getLongitudeInDegrees());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        //unbinder.unbind();
    }
}

package org.nearbyshops.enduser.UtilityGeocoding;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import org.nearbyshops.enduser.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by sumeet on 22/6/16.
 */
public class FetchAddressIntentService extends IntentService {


    protected ResultReceiver mReceiver;
    private static final String TAG = "applog";


    public FetchAddressIntentService(String name) {
        super(name);
    }


    public FetchAddressIntentService()
    {
        super("name");
    }





    private void deliverResultToReceiver(int resultCode, String message) {

        Bundle bundle = new Bundle();

        bundle.putString(Constants.RESULT_DATA_KEY, message);

        if(mReceiver!=null)
        {
            mReceiver.send(resultCode, bundle);
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        String errorMessage = "";

        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(
                Constants.LOCATION_DATA_EXTRA);


        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);



        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " +
                    location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 1; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }


            Log.i(TAG, getString(R.string.address_found));


            /*
            Log.i(TAG,"Admin Area : " +  address.getAdminArea()
                        + "\nFeature Name" + address.getFeatureName()
            + "\nLocality : " + address.getLocality()
            + "\nPostal code : " + address.getPostalCode()
            + "\nPremises : " + address.getPremises()
            + "\nSub locality : " + address.getSubLocality()
            + "\nThoroughfare :  " + address.getThoroughfare()
            + "\nSub admin Area : " + address.getSubAdminArea());



            Log.d(TAG,TextUtils.join(System.getProperty("line.separator"),
                    addressFragments));

            */


            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"),
                            addressFragments));
        }
    }



}

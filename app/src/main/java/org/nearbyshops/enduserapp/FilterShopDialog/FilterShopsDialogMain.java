package org.nearbyshops.enduserapp.FilterShopDialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.appyvet.rangebar.IRangeBarFormatter;
import com.appyvet.rangebar.RangeBar;

import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Utility.UtilityGeneral;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sumeet on 10/11/16.
 */

public class FilterShopsDialogMain extends DialogFragment implements RangeBar.OnRangeBarChangeListener {


    @Bind(R.id.rangebarDeliveryRange)
    RangeBar rangeBarDeliveryRange;

    @Bind(R.id.rangebarProximity)
    RangeBar rangeBarProximity;

    @Bind(R.id.textMax)
    TextView textMax;

    @Bind(R.id.textMin)
    TextView textMin;



    IRangeBarFormatter rangeBarFormatter = new IRangeBarFormatter() {

        @Override
        public String format(String value) {

            value = value + "" + " Km";

            return value;
        }

    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.dialog_filter_shop, container);
        ButterKnife.bind(this,view);
        updateRangeBars();


        return view;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // setup Range Bars
        rangeBarDeliveryRange.setFormatter(rangeBarFormatter);
        rangeBarProximity.setFormatter(rangeBarFormatter);

        rangeBarDeliveryRange.setOnRangeBarChangeListener(this);
        rangeBarProximity.setOnRangeBarChangeListener(this);



        ButterKnife.unbind(this);
    }





    float deliveryRangeMax = 0;
    float deliveryRangeMin= 0;
    float proximity = 0;
    boolean isDeliveryRangeUpdated = false;
    boolean isProximityUpdated = false;


    void updateRangeBars()
    {
        if(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY,30)>30)
        {
            return;
        }

        rangeBarDeliveryRange.setRangePinsByValue(
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY,0),
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY,30));


        if(UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY,30)>
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY,30))
        {
            return;
        }

        rangeBarProximity.setSeekPinByValue(
                UtilityGeneral.getFromSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY,30));

    }




    void savePreferences()
    {
        if(isDeliveryRangeUpdated)
        {
            if(deliveryRangeMax > 30 )
            {
                deliveryRangeMax = 30;
            }

            UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MIN_KEY,deliveryRangeMin);
            UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.DELIVERY_RANGE_MAX_KEY,deliveryRangeMax);
        }


        if(isProximityUpdated)
        {
            if(proximity>30)
            {
                proximity = 30;
            }


            UtilityGeneral.saveInSharedPrefFloat(UtilityGeneral.PROXIMITY_KEY,proximity);
        }
    }




    @Override
    public void onRangeChangeListener(
            RangeBar rangeBar, int leftPinIndex,
            int rightPinIndex, String leftPinValue, String rightPinValue) {


        switch (rangeBar.getId()) {
            case R.id.rangebarDeliveryRange:


                deliveryRangeMax = Float.parseFloat(rightPinValue);
                deliveryRangeMin = Float.parseFloat(leftPinValue);
                isDeliveryRangeUpdated = true;

                //Toast.makeText(this,"RangeBarFired",Toast.LENGTH_SHORT).show();

                break;

            case R.id.rangebarProximity:

                proximity = Float.parseFloat(rightPinValue);
                isProximityUpdated = true;

                //Toast.makeText(this,"RangeBarFired",Toast.LENGTH_SHORT).show();

                break;

            default:

                break;

        }


        if (rangeBar.getId() == R.id.rangebarDeliveryRange) {
            if (rightPinIndex > rangeBarProximity.getTickInterval()) {
                rangeBarProximity.setTickEnd(rightPinIndex);
                rangeBarProximity.setFormatter(rangeBarFormatter);
            }

            //textMax.setText("Max: " + rightPinValue + " Km");
            //textMin.setText("Min: " + leftPinValue + " Km");
        } else if (rangeBar.getId() == R.id.rangebarProximity) {
            textMax.setText("Max: " + rightPinValue + " Km");
            textMin.setText("Min: " + leftPinValue + " Km");
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        savePreferences();
    }
}

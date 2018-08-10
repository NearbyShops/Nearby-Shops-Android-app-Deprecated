package org.nearbyshops.enduserapp.Settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.nearbyshops.enduserapp.R;
import org.nearbyshops.enduserapp.Utility.PrefGeneral;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by sumeet on 15/1/17.
 */

public class FragmentSettingsCustom extends Fragment {


    @BindView(R.id.service_url)
    TextInputEditText service_url;
    @BindView(R.id.reset_button) TextView resetButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        ButterKnife.bind(this,rootView);
        setRetainInstance(true);

        bindDataToViews();

        return rootView;
    }


    void bindDataToViews()
    {
        service_url.setText(PrefGeneral.getServiceURL_SDS(getActivity()));
    }



    @OnTextChanged(R.id.service_url)
    void textChangeServiceURL()
    {
        PrefGeneral.saveServiceURL_SDS(service_url.getText().toString(),getActivity());
    }




    @OnClick(R.id.reset_button)
    void resetButtonClick()
    {
        PrefGeneral.saveServiceURL_SDS("http://sds.nearbyshops.org",getActivity());
        bindDataToViews();
    }



}

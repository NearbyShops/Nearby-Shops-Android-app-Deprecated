package org.nearbyshops.enduserappnew.EditProfile.ChangePhone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.nearbyshops.enduserappnew.ModelRoles.User;
import org.nearbyshops.enduserappnew.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 27/6/17.
 */

public class FragmentResultChangePhone extends Fragment {


    @BindView(R.id.account_credentials)
    TextView accountCredentials;

    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        View rootView = inflater.inflate(R.layout.fragment_result_change_phone, container, false);
        ButterKnife.bind(this,rootView);


        user = PrefChangePhone.getUser(getActivity());


        accountCredentials.setText("Phone : " + user.getPhone()
                + "\nPassword : XXXXX (Password is hidden)");

        return rootView;
    }




    @OnClick(R.id.finish_button)
    void finishButton()
    {

        // reset preferences
        PrefChangePhone.saveUser(null,getActivity());

        getActivity().setResult(10);
        getActivity().finish();
    }


}

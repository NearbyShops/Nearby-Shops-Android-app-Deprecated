package org.nearbyshops.enduser.Login;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import org.nearbyshops.enduser.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by sumeet on 10/10/16.
 */

public class ServiceURLDialog extends DialogFragment{


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_service_url, container);

        ButterKnife.bind(this,view);
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



    @OnClick(R.id.dialog_dismiss_icon)
    void dismissClick()
    {
        dismiss();
        Toast.makeText(getActivity(), R.string.dialog_dismissed,Toast.LENGTH_SHORT).show();
    }



    void showToastMessage(String message)
    {
        Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

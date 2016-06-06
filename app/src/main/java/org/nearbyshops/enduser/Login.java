package org.nearbyshops.enduser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.nearbyshops.enduser.Utility.UtilityGeneral;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class Login extends AppCompatActivity implements View.OnClickListener{

    @Bind(R.id.serviceURLEditText)
    EditText serviceUrlEditText;

    @Bind(R.id.distributorIDEdittext)
    EditText distributorIDEditText;

    @Bind(R.id.loginButton)
    Button loginButton;

    @Bind(R.id.signUpButton)
    Button signUpButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);



        serviceUrlEditText.setText(UtilityGeneral.getServiceURL(MyApplication.getAppContext()));
        distributorIDEditText.setText(String.valueOf(UtilityGeneral.getEndUserID(MyApplication.getAppContext())));

        serviceUrlEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                UtilityGeneral.saveServiceURL(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

                UtilityGeneral.saveServiceURL(s.toString());

            }
        });

        distributorIDEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().equals(new String(""))) {

                    UtilityGeneral.saveEndUserID(Integer.parseInt(s.toString()));
                }
            }
        });

    }




    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.loginButton:

                startActivity(new Intent(this,Home.class));

                break;

        }

    }


    @OnClick(R.id.loginButton)
    public void login()
    {
        startActivity(new Intent(this,Home.class));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

}

package com.example.a7bitstask.otp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.a7bitstask.R;
import com.google.android.material.snackbar.Snackbar;
import com.hbb20.CountryCodePicker;

public class MobileNumberGetActivity extends AppCompatActivity {

    CountryCodePicker ccp;
    String selectedCountryCode;
    private AppCompatButton generateOTP;
    private RelativeLayout relativeLayout;
    private EditText mobileNumberEdt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number_get);

        ccp = findViewById(R.id.ccp);
        generateOTP = findViewById(R.id.generateOTP);
        relativeLayout = findViewById(R.id.relativeLayout);
        mobileNumberEdt = findViewById(R.id.mobileNumber_edt);

        selectedCountryCode = ccp.getSelectedCountryCodeWithPlus();
        Log.v("tag", "Default Code" + selectedCountryCode);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selectedCountryCode = ccp.getSelectedCountryCodeWithPlus();
                Log.v("tag", "Select County Code" + selectedCountryCode);
            }
        });


        generateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = mobileNumberEdt.getText().toString();
                if (mobileNumberEdt.length() == 0) {
                    Snackbar snackbar = Snackbar.make(relativeLayout, R.string.mobil_number, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else if (mobileNumberEdt.length() < 10) {
                    Snackbar snackbar = Snackbar.make(relativeLayout, R.string.invalide_mobile_number, Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Intent intent = new Intent(MobileNumberGetActivity.this, OTPVerficiation.class);
                    intent.putExtra("mobileNumber", mobileNumber);
                    intent.putExtra("selectedCountryCode", selectedCountryCode);
                    startActivity(intent);
                }
            }
        });
    }
}
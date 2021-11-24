package com.example.a7bitstask.otp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.a7bitstask.R;
import com.example.a7bitstask.user.activity.ProfileCreateActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerficiation extends AppCompatActivity {


    FirebaseAuth firebaseAuth;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    private String mobileNumber;
    String code;
    private EditText input_one_editText, input_two_editText, input_three_editText, input_four_editText, input_five_editText, input_six_editText;
    private AppCompatButton verifyOTP;
    private TextView resend;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verficiation);

        mobileNumber = getIntent().getStringExtra("mobileNumber");
        Log.v("tag", "OTPMobileNumber" + mobileNumber);
        code = getIntent().getStringExtra("selectedCountryCode");

        firebaseAuth = FirebaseAuth.getInstance();
        onPhoneAuthentications(mobileNumber);

        progressDialog
                = new ProgressDialog(OTPVerficiation.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        input_one_editText = (EditText) findViewById(R.id.input_one_editText);
        input_two_editText = (EditText) findViewById(R.id.input_two_editText);
        input_three_editText = (EditText) findViewById(R.id.input_three_editText);
        input_four_editText = (EditText) findViewById(R.id.input_four_editText);
        input_five_editText = (EditText) findViewById(R.id.input_five_editText);
        input_six_editText = (EditText) findViewById(R.id.input_six_editText);
        verifyOTP = (AppCompatButton) findViewById(R.id.verifyOTP_button);
        resend = findViewById(R.id.resend_text);

        EditText[] edit = {input_one_editText, input_two_editText, input_three_editText, input_four_editText,
                input_five_editText, input_six_editText};

        input_one_editText.addTextChangedListener(new GenericTextWatcher(input_one_editText, edit));
        input_two_editText.addTextChangedListener(new GenericTextWatcher(input_two_editText, edit));
        input_three_editText.addTextChangedListener(new GenericTextWatcher(input_three_editText, edit));
        input_four_editText.addTextChangedListener(new GenericTextWatcher(input_four_editText, edit));
        input_five_editText.addTextChangedListener(new GenericTextWatcher(input_five_editText, edit));
        input_six_editText.addTextChangedListener(new GenericTextWatcher(input_six_editText, edit));

        verifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpValue =
                        input_one_editText.getText().toString() + input_two_editText.getText().toString()
                                + input_three_editText.getText().toString()
                                + input_four_editText.getText().toString()
                                + input_five_editText.getText().toString()
                                + input_six_editText.getText().toString();
                if (otpValue.length() != 0) {
                    verifyVerificationCode(otpValue, mobileNumber);
                } else {
                    Toast.makeText(OTPVerficiation.this, String.valueOf(R.string.invalidOTP), Toast.LENGTH_SHORT).show();
                }
            }
        });


        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode();
            }
        });
    }

    public void onPhoneAuthentications(String mobileNumber) {


        PhoneAuthOptions phoneAuthOptions =
                PhoneAuthOptions.newBuilder(firebaseAuth).setPhoneNumber("+91" + mobileNumber)
                        .setTimeout(100L, TimeUnit.SECONDS).setActivity(OTPVerficiation.this)
                        .setCallbacks(mCallbacks).build();
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();


            if (code != null) {
                //editTextCode.setText(code);
                verifyVerificationCode(code, mobileNumber);
                progressDialog.dismiss();
            } else {
                Toast.makeText(OTPVerficiation.this, "Some thing Went Wrong", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OTPVerficiation.this, String.valueOf(R.string.verificationFailed), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            mVerificationId = s;
            mResendToken = forceResendingToken;
        }

        @Override
        public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
            super.onCodeAutoRetrievalTimeOut(s);
        }
    };

    private void verifyVerificationCode(String otp, String mobileNumber) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);

        signInWithPhoneAuthCredential(credential, mobileNumber);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential, String mobileNumber) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPVerficiation.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.v("tag", "OTP Entered MobileNumber" + mobileNumber);
                            Intent intent = new Intent(OTPVerficiation.this, ProfileCreateActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("mobielNumber", mobileNumber);
                            startActivity(intent);

                        } else {


                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }


    public class GenericTextWatcher implements TextWatcher {
        private final EditText[] editText;
        private View view;

        public GenericTextWatcher(View view, EditText editText[]) {
            this.editText = editText;
            this.view = view;
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.input_one_editText:
                    if (text.length() == 1)
                        editText[1].requestFocus();
                    break;
                case R.id.input_two_editText:

                    if (text.length() == 1)
                        editText[2].requestFocus();
                    else if (text.length() == 0)
                        editText[0].requestFocus();
                    break;
                case R.id.input_three_editText:
                    if (text.length() == 1)
                        editText[3].requestFocus();
                    else if (text.length() == 0)
                        editText[1].requestFocus();
                    break;
                case R.id.input_four_editText:
                    if (text.length() == 1) {
                        editText[4].requestFocus();
                    } else if (text.length() == 0) {
                        editText[2].requestFocus();
                    }
                    break;
                case R.id.input_five_editText:
                    if (text.length() == 1) {
                        editText[5].requestFocus();
                    } else if (text.length() == 0) {
                        editText[3].requestFocus();
                    }
                    break;
                case R.id.input_six_editText:
                    if (text.length() == 0) {
                        editText[4].requestFocus();
                    }
                    break;
            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    }


    public void resendVerificationCode() {

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(mobileNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(OTPVerficiation.this)
                .setCallbacks(mCallbacks)
                .setForceResendingToken(mResendToken)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}


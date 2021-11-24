package com.example.a7bitstask.user.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a7bitstask.R;
import com.example.a7bitstask.user.UserDataSession;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class ProfilePageActivity extends AppCompatActivity {


    private CircularImageView profileCircularImageView;
    private TextView nameTxtView, mobileNumberTextView, emailTxtView;
    private String imageURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        nameTxtView = findViewById(R.id.name_txtView);
        mobileNumberTextView = findViewById(R.id.mobileNumber_textView);
        emailTxtView = findViewById(R.id.email_txtView);
        profileCircularImageView = findViewById(R.id.profile_circularImageView);

        UserDataSession userDataSession = new UserDataSession(getApplicationContext());
        nameTxtView.setText(userDataSession.getUSerValue("name"));
        mobileNumberTextView.setText(userDataSession.getUSerValue("mobileNumber"));
        emailTxtView.setText(userDataSession.getUSerValue("email"));

        imageURI = userDataSession.getUSerValue("imageURI");
        Picasso.get().load(imageURI).into(profileCircularImageView);


    }
}
package com.example.a7bitstask.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserDataSession {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public UserDataSession(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }


    public void setUserValue(String name, String email, String imageURI, String mobileNumber) {
        editor.putString("name", name)
                .putString("email", email)
                .putString("imageURI", imageURI)
                .putString("mobileNumber", mobileNumber).apply();
    }

    public String getUSerValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}

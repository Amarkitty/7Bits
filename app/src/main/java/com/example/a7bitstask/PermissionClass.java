package com.example.a7bitstask;

import android.Manifest;
import android.content.Context;

public class PermissionClass {

    private Context context;

    public PermissionClass(Context context) {
        this.context = context;
    }

    //Collection of Permissions
    public String[] permissionsArray() {
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.CALL_PHONE

        };
        return PERMISSIONS;
    }
}

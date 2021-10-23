package com.ascs.harambeee_studentportal.Handler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ascs.harambeee_studentportal.MainActivity;

public class PermissionHandler {

    private static final int PERMISSION_REQUEST = 1;
    Context context;
    private MainActivity mainActivity;
    private FingerprintManager fingerprintManager;

    public PermissionHandler(Context context) {
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPrem() {
        int premssionCon = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_NETWORK_STATE);
        int premssionStore = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int premissionCall = ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE);
        int premissionIntrnet = ContextCompat.checkSelfPermission(context,Manifest.permission.INTERNET);

        if (premssionCon != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSION_REQUEST);
        }
        if (premssionStore != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
        }
        if (premissionCall !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST);
        }
        if (premissionIntrnet !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.INTERNET},PERMISSION_REQUEST);
        }
    }
}

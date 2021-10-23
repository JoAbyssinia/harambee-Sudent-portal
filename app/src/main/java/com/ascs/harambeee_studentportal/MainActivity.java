package com.ascs.harambeee_studentportal;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.agrawalsuneet.dotsloader.loaders.CircularDotsLoader;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ascs.harambeee_studentportal.ConnectionHandler.Connector;
import com.ascs.harambeee_studentportal.ConnectionHandler.Urls;
import com.ascs.harambeee_studentportal.Handler.PermissionHandler;
import com.ascs.harambeee_studentportal.Storages.UserDataStorage;
import com.ascs.harambeee_studentportal.UI.MasterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    CircularDotsLoader loader;
    UserDataStorage storage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        PermissionHandler handler = new PermissionHandler(this);
        handler.checkPrem();

        loader = findViewById(R.id.loader);
        loader.setDefaultColor(ContextCompat.getColor(this, R.color.primaryColor));
        loader.setSelectedColor(ContextCompat.getColor(this, R.color.secondaryColor));
        loader.setShowRunningShadow(true);
        loader.setFirstShadowColor(ContextCompat.getColor(this, R.color.secondaryLightColor));
        loader.setSecondShadowColor(ContextCompat.getColor(this, R.color.secondaryDarkColor));
        loader.setVisibility(View.INVISIBLE);

        Connector connector = new Connector();
        connector.connect(Urls.URL);
        storage = new UserDataStorage(this);
        int SPLASH_SCREEN = 3000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if (isAccount()) {
                    String[] account = storage.getEmailPass().split(" ");

                    String URLBulder = Urls.LOGIN + "email=" + URLDecoder.decode(account[0].trim() + "&" + "password=" + account[1].trim());
                    logIn(URLBulder);
                } else {

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }
        }, SPLASH_SCREEN);
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    private void logIn(final String login) {
        loader.setVisibility(View.VISIBLE);
        Log.d("urlR", login);
        StringRequest request = new StringRequest(Request.Method.GET, login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")) {

                        startActivity(new Intent(MainActivity.this, MasterActivity.class));
                        finish();
                    } else {

                        SweetAlertDialog unknown = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
                        unknown.setContentText("Unknown student's account, check your Id and password or Contact Academic office or try again");
                        unknown.setConfirmText("Log-in");
                        unknown.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                storage.setEmailPassId("0", "0");
                                storage.setStudentRealID("0");
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
                        unknown.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setContentText("No internet connection, try again ")
                        .setCancelButton("Close", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        })
                        .setConfirmButton("goto setting", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Toast.makeText(MainActivity.this, "go to setting and turn on wifi or data", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    //    account checker
    private boolean isAccount() {
        final UserDataStorage usernPass = new UserDataStorage(this);
        if (usernPass.getEmailPass().equals("0")) {
            return false;
        } else {
            return true;
        }

    }

}

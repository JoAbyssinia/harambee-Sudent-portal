package com.ascs.harambeee_studentportal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ascs.harambeee_studentportal.ConnectionHandler.Urls;
import com.ascs.harambeee_studentportal.Handler.PermissionHandler;
import com.ascs.harambeee_studentportal.Storages.UserDataStorage;
import com.ascs.harambeee_studentportal.UI.MasterActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    static String getPass;
    TextInputEditText email, pass;
    TextInputLayout layoutemail, layoutpassword;
    Button btnLogin;
    private long presstime;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        email = findViewById(R.id.emailTf);
        pass = findViewById(R.id.passTf);
        btnLogin = findViewById(R.id.login_btn);
        layoutemail = findViewById(R.id.layoutemail);
        layoutpassword = findViewById(R.id.layoutpassword);

        PermissionHandler handler = new PermissionHandler(this);
        handler.checkPrem();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginValidation(email.getText().toString().trim(), pass.getText().toString().trim());
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutemail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                layoutpassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pass.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    loginValidation(email.getText().toString().trim(), pass.getText().toString().trim());
                }
                return false;
            }
        });

    }

    private void loginValidation(String emailS, String passS) {

        if (TextUtils.isEmpty(emailS)) {
            layoutemail.setErrorEnabled(true);
            layoutemail.setError("Enter Student email account");
        }
        if (TextUtils.isEmpty(passS)) {
            layoutpassword.setErrorEnabled(true);
            layoutpassword.setError("Enter password");
        }
        if (!TextUtils.isEmpty(emailS) && !TextUtils.isEmpty(passS)) {

            String urlBiulder = Urls.LOGIN + "email=" + URLDecoder.decode(emailS) + "&password=" + passS;
            logIn(urlBiulder, URLDecoder.decode(emailS), passS);

        }


    }

    private void logIn(final String login, final String emailS, final String passS) {

        final UserDataStorage storage = new UserDataStorage(this);

        final SweetAlertDialog auth = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        auth.setContentText("Authenticating...");
        auth.setCancelable(false);
        auth.show();

        StringRequest request = new StringRequest(Request.Method.GET, login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")) {

                        storage.setEmailPassId(emailS + " " + passS, object.getString("user"));

                        String proUrl = Urls.PROFILES + object.getString("user");

                        StringRequest Id = new StringRequest(Request.Method.GET, proUrl, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject objects = new JSONObject(response);
                                    storage.setStudentRealID(objects.getString("id"));
                                    Intent master = new Intent(LoginActivity.this, MasterActivity.class);
                                    startActivity(master);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                auth.dismiss();
                                Log.d("student_id", "Cant find the Id now ");
                                SweetAlertDialog unknown = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                                unknown.setContentText("Internal server error, try again later");
                                unknown.show();

                                storage.setEmailPassId("0", "0");
                                storage.setStudentRealID("0");

                            }
                        });
                        Volley.newRequestQueue(LoginActivity.this).add(Id);

                    } else {
                        auth.dismissWithAnimation();


                        SweetAlertDialog unknown = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
                        unknown.setContentText("Unknown student's account, check your Id and password or Contact Academic office or try again");
                        unknown.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                auth.dismissWithAnimation();

                SweetAlertDialog no = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE);
                no.setContentText("No internet connection, try again.");
                no.setCancelable(false);
                no.show();

            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    public void onBackPressed() {
        if (presstime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            this.finishAffinity();
        } else {
            Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();
        }

        presstime = System.currentTimeMillis();


    }
}

package com.ascs.harambeee_studentportal;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ascs.harambeee_studentportal.ConnectionHandler.Urls;
import com.ascs.harambeee_studentportal.Storages.UserDataStorage;
import com.ascs.harambeee_studentportal.UI.MasterActivity;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PasswordChangeActivity extends AppCompatActivity {

    EditText old, nw, confirm;
    TextInputLayout oldLayout, newLayout, confirmLayout;
    Button btnCh;
    UserDataStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        setTitle("Change Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        old = findViewById(R.id.oldpassvalue);
        nw = findViewById(R.id.newpassvalue);
        confirm = findViewById(R.id.confirmpasswodvalue);
        btnCh = findViewById(R.id.passchangebtn);

        oldLayout = findViewById(R.id.oldpass);
        newLayout = findViewById(R.id.newpass);
        confirmLayout = findViewById(R.id.confirmpasswod);

        storage = new UserDataStorage(this);




        btnCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation(old.getText().toString().trim(), nw.getText().toString().trim(), confirm.getText().toString().trim());
            }
        });


        old.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldLayout.setErrorEnabled(false);
                btnCh.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable s) {
                String[] account = storage.getEmailPass().split(" ");
                if (!s.toString().equals(account[1])) {
                    oldLayout.setErrorEnabled(true);
                    oldLayout.setError("password not match");
                    btnCh.setEnabled(false);

                }
            }
        });

        nw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newLayout.setErrorEnabled(false);
                btnCh.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() < 6) {
                    newLayout.setErrorEnabled(true);
                    newLayout.setError("Min password length is 6");
                    btnCh.setEnabled(false);
                }

                if (s.toString().equalsIgnoreCase("default123")) {
                    newLayout.setErrorEnabled(true);
                    newLayout.setError("Shouldn't use password same as default one");
                    btnCh.setEnabled(false);
                }


            }
        });

        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nw.getText().toString().trim().length() >= 6 && !nw.getText().toString().equalsIgnoreCase("default123")) {
                    confirmLayout.setErrorEnabled(false);
                    btnCh.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nw.getText().toString().trim().length() >= 6) {
                    if (!nw.getText().toString().trim().equals(s.toString())) {
                        confirmLayout.setErrorEnabled(true);
                        confirmLayout.setError("password not match");
                        btnCh.setEnabled(false);
                    }
                }
            }
        });

        confirm.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    validation(old.getText().toString().trim(), nw.getText().toString().trim(), confirm.getText().toString().trim());
                }
                return false;
            }
        });

    }

    private void validation(final String oldS, final String nwS, String passS) {
        if (TextUtils.isEmpty(oldS)) {
            oldLayout.setErrorEnabled(true);
            oldLayout.setError("Enter old password");
        }

        if (TextUtils.isEmpty(nwS)) {
            newLayout.setErrorEnabled(true);
            newLayout.setError("Enter New password");
        }

        if (TextUtils.isEmpty(passS)) {
            confirmLayout.setErrorEnabled(true);
            confirmLayout.setError("Enter again new password");
        }

        if (!TextUtils.isEmpty(oldS) && !TextUtils.isEmpty(nwS) && !TextUtils.isEmpty(passS)) {

            if (!oldS.equalsIgnoreCase(nwS)) {

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Password Change Confirmation")
                        .setContentText("You want to change your password?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();

                                changePassword(oldS, nwS);

                            }
                        })
                        .setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();

            } else {
                newLayout.setErrorEnabled(true);
                newLayout.setError("new password is similar to old password. please change to new strong password");
            }
        }
    }

    public void changePassword(String oldPassword, final String newPassword) {

        String id = storage.getStudentID();
        String URLBuilder = Urls.HOME + Urls.PASSWORD_CHANGE + "user_id=" + id + "&old_password=" + oldPassword + "&new_password=" + newPassword;

        final SweetAlertDialog dialog;


        dialog = new SweetAlertDialog(PasswordChangeActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.setTitleText("Changing ...");
        dialog.setCancelable(false);
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URLBuilder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    dialog.dismissWithAnimation();

                    if (object.getString("status").equals("200")) {

                        new SweetAlertDialog(PasswordChangeActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setContentText(object.getString("message"))
                                .setConfirmButton("okay", new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                                        String[] ID = storage.getEmailPass().split(" ");

                                        storage.setEmailPass(ID[0] + " " + newPassword);

                                        startActivity(new Intent(PasswordChangeActivity.this, MasterActivity.class));
                                        finish();
                                    }
                                })
                                .show();


                    } else if (object.getString("status").equals("400")) {
                        new SweetAlertDialog(PasswordChangeActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText(object.getString("message") + " try a gain. ")
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("passwordchange", "" + error.getMessage());
            }
        });

        Volley.newRequestQueue(PasswordChangeActivity.this).add(request);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.ascs.harambeee_studentportal.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ascs.harambeee_studentportal.LoginActivity;
import com.ascs.harambeee_studentportal.PasswordChangeActivity;
import com.ascs.harambeee_studentportal.R;
import com.ascs.harambeee_studentportal.Storages.UserDataStorage;
import com.ascs.harambeee_studentportal.UI.fragments.GradeFragment;
import com.ascs.harambeee_studentportal.UI.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MasterActivity extends AppCompatActivity {

    BottomNavigationView navigationView;
    FragmentManager manager = getSupportFragmentManager();
    Fragment fragment;
    UserDataStorage storage;
    private long timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        getSupportActionBar().hide();
        storage = new UserDataStorage(this);
        String[] pass = storage.getEmailPass().split(" ");
        if (pass[1].equalsIgnoreCase("default123")) {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setContentText("Your password is default. please change it as soon as possible.")
                    .setConfirmText("change")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            startActivity(new Intent(MasterActivity.this, PasswordChangeActivity.class));
                        }
                    })
                    .setCancelButton("Not now", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    })
                    .show();


        }


        navigationView = findViewById(R.id.bottomnave);
        fragment = new GradeFragment(MasterActivity.this);
        fragmentTransact(fragment);


        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        fragment = new ProfileFragment(MasterActivity.this);
                        fragmentTransact(fragment);
                        return true;
                    case R.id.grade:
                        fragment = new GradeFragment(MasterActivity.this);
                        fragmentTransact(fragment);
                        return true;
                    case R.id.announcement:
                        Toast.makeText(MasterActivity.this, "its announcement", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.logoutnav:
                        logout();
                        return true;
                }

                return false;
            }
        });
    }

    private void logout() {


        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setContentText("Are you sure ?")
                .setCustomImage(R.drawable.logo)
                .setCancelText("Log-out")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        storage.setEmailPassId("0", "0");
                        storage.setStudentRealID("0");
                        startActivity(new Intent(MasterActivity.this, LoginActivity.class));
                    }
                })
                .setConfirmButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();

    }

    private void fragmentTransact(Fragment fragment) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (timer + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            this.finishAffinity();
        } else {
            Toast.makeText(this, "press again to Exit", Toast.LENGTH_SHORT).show();
            timer = System.currentTimeMillis();
        }

    }
}

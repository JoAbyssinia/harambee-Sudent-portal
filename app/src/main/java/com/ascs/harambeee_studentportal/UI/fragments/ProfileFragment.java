package com.ascs.harambeee_studentportal.UI.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ascs.harambeee_studentportal.ConnectionHandler.Urls;
import com.ascs.harambeee_studentportal.JSON.JsonDownload;
import com.ascs.harambeee_studentportal.LoginActivity;
import com.ascs.harambeee_studentportal.PasswordChangeActivity;
import com.ascs.harambeee_studentportal.R;
import com.ascs.harambeee_studentportal.Storages.UserDataStorage;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    Context context;
    TextView[] views;
    CircleImageView ppic;
    TextView chPassword;

    public ProfileFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        views = new TextView[]{view.findViewById(R.id.accountid), view.findViewById(R.id.accountname), view.
                findViewById(R.id.gender), view.findViewById(R.id.department), view.findViewById(R.id.ayear), view.
                findViewById(R.id.division), view.findViewById(R.id.section), view.
                findViewById(R.id.phonen), view.findViewById(R.id.email_pro), view.findViewById(R.id.program),view.findViewById(R.id.yearTop),
        view.findViewById(R.id.semisterPro)};
        ppic = view.findViewById(R.id.profile_image);

        chPassword = view.findViewById(R.id.chPassword);

        UserDataStorage dataStorage = new UserDataStorage(context);
        String sid = dataStorage.getStudentID();
//        Toast.makeText(context, ""+Urls.PROFILES + sid, Toast.LENGTH_SHORT).show();
        new JsonDownload(context, views, ppic, 1).execute(Urls.PROFILES + sid);

        chPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(context, PasswordChangeActivity.class));

            }
        });


        return view;
    }

    private void eraseData() {
        UserDataStorage storage = new UserDataStorage(context);
        storage.setEmailPassId("0","0");
        storage.setStudentRealID("0");
    }


}

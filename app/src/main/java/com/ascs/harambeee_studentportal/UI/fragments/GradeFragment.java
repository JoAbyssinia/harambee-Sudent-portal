package com.ascs.harambeee_studentportal.UI.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ascs.harambeee_studentportal.ConnectionHandler.Urls;
import com.ascs.harambeee_studentportal.JSON.JsonDownload;
import com.ascs.harambeee_studentportal.R;
import com.ascs.harambeee_studentportal.Storages.UserDataStorage;

import org.json.JSONObject;


public class GradeFragment extends Fragment {

    Context context;
    TextView[] gViews,cViews;
    ListView gradeListView;

    public GradeFragment(Context context) {
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grade, container, false);
        UserDataStorage storage = new UserDataStorage(context);

        gViews = new TextView[]{view.findViewById(R.id.depG),
                view.findViewById(R.id.proG),
                view.findViewById(R.id.batchG),
                view.findViewById(R.id.sectionG),
                view.findViewById(R.id.admissionG),
                view.findViewById(R.id.semG)};

        cViews = new TextView[]{
                view.findViewById(R.id.currentTotal),
                view.findViewById(R.id.currentPoint),
                view.findViewById(R.id.currentGPA),
                view.findViewById(R.id.cumulativeTotal),
                view.findViewById(R.id.cumulativePoint),
                view.findViewById(R.id.cumulativeGPA),
        };

        profileFetch(gViews);
        gradeListView = view.findViewById(R.id.gradelistview);


        Log.d("grade_api", "" + Urls.GRADE + storage.getStudentRealID());
        new JsonDownload(context, gradeListView, cViews,2).execute(Urls.GRADE + storage.getStudentRealID());

        return view;
    }

    private void profileFetch(final TextView[] gViews) {

        UserDataStorage storage = new UserDataStorage(context);
        String URL_Builder = Urls.PROFILES + storage.getStudentID();

        StringRequest prof = new StringRequest(Request.Method.GET, URL_Builder, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject object = new JSONObject(response);

                    gViews[0].setText(object.getJSONObject("curriculum").getString("name"));
                    gViews[1].setText(object.getString("program"));
                    gViews[2].setText(object.getString("year"));
                    gViews[3].setText(object.getString("section"));
                    gViews[4].setText(object.getString("addmision_type"));
                    gViews[5].setText(object.getString("semester"));


                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Can't find profile", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(context).add(prof);


    }

}

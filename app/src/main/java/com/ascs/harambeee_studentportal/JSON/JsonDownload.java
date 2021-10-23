package com.ascs.harambeee_studentportal.JSON;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.ascs.harambeee_studentportal.ConnectionHandler.Connector;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class JsonDownload extends AsyncTask<String, Void, String> {
    ProgressDialog dialog;
    SweetAlertDialog alertDialog;
    Context context;
    TextView[] views,cViews;
    CircleImageView Propic;
    ListView listView;


    private int location;


    public JsonDownload(Context context, TextView[] views, CircleImageView propic, int i) {
        this.context = context;
        this.views = views;
        this.location = i;
        this.Propic = propic;
    }

    public JsonDownload(Context context,ListView gradeListView, TextView[] cViews,  int i) {
        this.context = context;
        this.listView = gradeListView;
        this.cViews = cViews;
        this.location = i;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        alertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        alertDialog.setTitleText("Loading ...");
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    @Override
    protected String doInBackground(String... url) {


        if (Connector.connect(url[0]).toString().startsWith("error")) {
            Log.d("json", "doInBackground: error ");
        } else {

            HttpURLConnection urlConnection = null;
            URL ur;

            try {
                ur = new URL(url[0]);
                urlConnection = (HttpURLConnection) ur.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                String line;

                StringBuilder jsonDate = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    jsonDate.append(line).append("\n");
                }

                reader.close();
                stream.close();
                return jsonDate.toString();


            } catch (IOException e) {
                return e.getMessage();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        alertDialog.dismissWithAnimation();

        if (location == 1) {
            new ProfileJSONParsor(context, views, Propic, result).execute();
        } else if (location == 2) {
            new GradeJSONParsor(context, result, listView , cViews).execute();
        }


    }
}

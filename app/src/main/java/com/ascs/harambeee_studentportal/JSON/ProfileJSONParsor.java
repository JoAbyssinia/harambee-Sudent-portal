package com.ascs.harambeee_studentportal.JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileJSONParsor extends AsyncTask <Void,Void,Boolean> {

    Context context;
    TextView[] views;
    ImageView propic;
    String result;



    public ProfileJSONParsor(Context context, TextView[] views, ImageView propic, String result) {

        this.context =context;
        this.views = views;
        this.propic =propic;
        this.result =result;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        if (aBoolean){
            try {
                Log.d("result", result);
                JSONObject object = new JSONObject(result);
                views[0].setText(object.getString("id_number"));

                String builder = object.getString("personal_name") +" "+
                        object.getString("father_name") +" "+
                        object.getString("grand_father_name");
                views[1].setText(builder);
                views[2].setText( object.getJSONObject("gender").getString("name"));
                views[3].setText(object.getJSONObject("curriculum").getString("name"));
                views[4].setText(object.getString("batch_year"));
                views[5].setText(object.getString("addmision_type"));
                views[6].setText(object.getString("section"));
                views[7].setText(String.format("0%s", object.getString("mobile_number")));
                views[8].setText(object.getString("email"));
                views[9].setText(object.getString("program"));
                views[10].setText(object.getString("year"));
                views[11].setText(object.getString("semester"));

                Toast.makeText(context,object.getString("remark"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

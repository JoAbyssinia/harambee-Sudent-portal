package com.ascs.harambeee_studentportal.JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ascs.harambeee_studentportal.Adapter.ListViewAdapter;
import com.ascs.harambeee_studentportal.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class GradeJSONParsor extends AsyncTask<Void, Void, String> {

    Context context;
    String result, semr;
    ListView listView;
    TextView[] cViews;
    int tHr = 0;
    float gradepoint = 0;
    float gpa = (float) 0.0;


    ArrayList<String> ttl = new ArrayList<>();
    ArrayList<String> ch = new ArrayList<>();
    ArrayList<String> gradeS = new ArrayList<>();


    public GradeJSONParsor(Context context, String result, ListView listView, TextView[] cViews) {
        this.context = context;
        this.result = result;
        this.listView = listView;
        this.cViews = cViews;
    }


    @Override
    protected String doInBackground(Void... voids) {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            JSONArray array = jsonObject.getJSONArray("records");
            Log.d("all", String.valueOf(array));

            JSONObject obj = array.getJSONObject(3);
            Log.d("lenth", String.valueOf(array.length()));


            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);

                Log.d("ppss", object.getString("grade_latter"));
                ttl.add(object.getString("name") + " (" + object.getString("code") + ")");
                ch.add(object.getString("credit_hours"));

                semr = (object.getString("semester"));


                if (object.getString("grade_latter").trim().equalsIgnoreCase("null")) {
                    gradeS.add("-");
                } else {
                    gradeS.add(object.getString("grade_latter").toUpperCase());
                }


                tHr += Integer.valueOf(object.getString("credit_hours"));

                gpaCalculater(object.getString("grade_latter"), Integer.valueOf(object.getString("credit_hours")));


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (tHr != 0 && gradepoint != 0) {
            gpa = gradepoint / tHr;

            cViews[0].setText(String.valueOf(tHr));
            cViews[1].setText(String.valueOf(gradepoint));
            cViews[2].setText(String.valueOf(gpa));
            cViews[3].setText(String.valueOf(tHr));
            cViews[4].setText(String.valueOf(gradepoint));
            cViews[5].setText(String.valueOf(gpa));

        } else {
            Toast.makeText(context, "Grade not yet submitted", Toast.LENGTH_SHORT).show();
        }

        ListViewAdapter adapter = new ListViewAdapter(context, ttl, ch, gradeS);
        listView.setAdapter(adapter);
        LayoutAnimationController animationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_anim_slide_right);
        listView.setLayoutAnimation(animationController);
    }


    public void gpaCalculater(String grade_latter, int CH) {

        if (grade_latter.equalsIgnoreCase("a+")) {
            gradepoint += CH * 4;
        } else if (grade_latter.equalsIgnoreCase("a")) {
            gradepoint += CH * 4;
        } else if (grade_latter.equalsIgnoreCase("a-")) {
            gradepoint += CH * 3.75;
        } else if (grade_latter.equalsIgnoreCase("b+")) {
            gradepoint += CH * 3.5;
        } else if (grade_latter.equalsIgnoreCase("b")) {
            gradepoint += CH * 3;
        } else if (grade_latter.equalsIgnoreCase("b-")) {
            gradepoint += CH * 2.75;
        } else if (grade_latter.equalsIgnoreCase("c+")) {
            gradepoint += CH * 2.5;
        } else if (grade_latter.equalsIgnoreCase("c")) {
            gradepoint += CH * 2;
        } else if (grade_latter.equalsIgnoreCase("c-")) {
            gradepoint += CH * 1.75;
        } else if (grade_latter.equalsIgnoreCase("d")) {
            gradepoint += CH;
        } else if (grade_latter.equalsIgnoreCase("f") ||
                grade_latter.equalsIgnoreCase("I") ||
                grade_latter.equalsIgnoreCase("ng")) {
            gradepoint += 0;
        }

    }
}

package com.ascs.harambeee_studentportal.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ascs.harambeee_studentportal.R;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter {

    Context context;
    private ArrayList<String> cTtl, cHr, cGrd;

    public ListViewAdapter(Context context, ArrayList<String> cTtl, ArrayList<String> cHr, ArrayList<String> cGrd) {
        super(context, R.layout.grade_row, R.id.grade_row_title, cTtl);
        this.cTtl = cTtl;
        this.cHr = cHr;
        this.cGrd = cGrd;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.grade_row, parent, false);

        TextView ttl = view.findViewById(R.id.grade_row_title);
        TextView hr = view.findViewById(R.id.grade_row_ch);
        TextView grd = view.findViewById(R.id.grade_row_g);

        ttl.setText(cTtl.get(position));
        hr.setText(cHr.get(position));
        grd.setText(cGrd.get(position));


        return view;
    }
}

package com.divide.ibitech.divide_ibitech;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String>{

    public CustomAdapter(@NonNull Context context, String[] symptoms) {
        super(context, R.layout.custom_row ,symptoms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row, parent, false);

        String singleSymptom = getItem(position);
        TextView text = customView.findViewById(R.id.txtName);
        //ImageView image = customView.findViewById(R.id.imgProfilePic);

        text.setText(singleSymptom);
        //image.setImageResource(R.drawable.profilepic);
        return customView;
    }
}

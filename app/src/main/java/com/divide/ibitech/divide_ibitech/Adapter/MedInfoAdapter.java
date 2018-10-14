package com.divide.ibitech.divide_ibitech.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.divide.ibitech.divide_ibitech.R;

public class MedInfoAdapter extends ArrayAdapter<String> {

     public MedInfoAdapter(Context context, String[] medrecInfo) {
        super(context, R.layout.custom_med_rec_row, medrecInfo);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater medrecInflater = LayoutInflater.from(getContext());
        View myVeiw = medrecInflater.inflate(R.layout.custom_med_rec_row,parent,false );

        String medRecCat = getItem(position);
        TextView category = myVeiw.findViewById(R.id.tv_category);
        ImageView imageView = myVeiw.findViewById(R.id.img_pic);

        category.setText(medRecCat);
        imageView.setImageResource(R.drawable.ic_lock_black_24dp);

        return myVeiw;
    }
}

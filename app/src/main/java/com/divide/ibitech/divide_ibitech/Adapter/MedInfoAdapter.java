package com.divide.ibitech.divide_ibitech.Adapter;

import android.app.Activity;
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

    private String[] cats;
    private Integer[] imgid;
    private Activity context;

     public MedInfoAdapter(Activity context, String[] medrecInfo, Integer[] imgid) {
        super(context, R.layout.custom_med_rec_row, medrecInfo);
        this.context = context;
        this.cats = medrecInfo;
        this.imgid = imgid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

         View r = convertView;
         ViewHolder viewHolder;
         if (r == null){
             LayoutInflater layoutInflater = context.getLayoutInflater();
             r = layoutInflater.inflate(R.layout.custom_med_rec_row,null,true );
             viewHolder = new ViewHolder(r);
             r.setTag(viewHolder);
         }
         else {
            viewHolder = (ViewHolder) r.getTag();
         }
         viewHolder.tv.setText(cats[position]);
         viewHolder.iv.setImageResource(imgid[position]);

         return r;
    }

    //optimizes the list view performance
    class ViewHolder {

         TextView tv;
         ImageView iv;

         ViewHolder(View v){
             tv = v.findViewById(R.id.tv_category);
             iv = v.findViewById(R.id.img_pic);
         }

    }
}

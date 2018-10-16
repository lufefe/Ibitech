package com.divide.ibitech.divide_ibitech.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.divide.ibitech.divide_ibitech.Models.AllergyList;
import com.divide.ibitech.divide_ibitech.R;

import java.util.List;

public class AllergyListAdapter extends ArrayAdapter<AllergyList> {
    List<AllergyList> allergyList;
    private Context mCtx;

    public AllergyListAdapter(List<AllergyList> Al, Context c) {
        super(c, R.layout.allergy_list,Al);
        this.allergyList=Al;
        this.mCtx=c;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.allergy_list,null,true);
        TextView condition_name =(TextView) view.findViewById(R.id.etAName);
        TextView date_added =(TextView) view.findViewById(R.id.etAVisDate);
        AllergyList allergy =allergyList.get(position);
        condition_name.setText(allergy.getAllergy_name());
        date_added.setText(allergy.getDate_added());


        return view;
    }
}
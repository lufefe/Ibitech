package com.envy.patrema.envy_patrema.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.ConditionList;
//import com.divide.ibitech.divide_ibitech.Models.SymptomsList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;


public class ConditionListAdapter extends ArrayAdapter<ConditionList> {
    private   List<ConditionList> conditionLists;
    private Context mCtx;

    public ConditionListAdapter( List<ConditionList> Co, Context c) {
        super(c, R.layout.condition_list, Co);
        this.conditionLists=Co;
        this.mCtx=c;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view= inflater.inflate(R.layout.condition_list,null,true);
        TextView condition_name = view.findViewById(R.id.etCoName);
        TextView date_added = view.findViewById(R.id.etCoDate);
        TextView doctor_name = view.findViewById(R.id.txtDoctorName);

        ConditionList conditionList =  conditionLists.get(position);
        condition_name.setText(conditionList.getCondition_name());
        date_added.setText(conditionList.getDate_added());
        doctor_name.setText(String.format("Dr %s %s", conditionList.getDoctorName(), conditionList.getDoctorSurname()));

        return view;
    }
}
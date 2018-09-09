package com.divide.ibitech.divide_ibitech.Adapter;
/**
 * Created by s216100801
 */
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.divide.ibitech.divide_ibitech.Models.ConditionList;
//import com.divide.ibitech.divide_ibitech.Models.SymptomsList;
import com.divide.ibitech.divide_ibitech.R;

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
        TextView condition_name =(TextView) view.findViewById(R.id.etCoName);
        TextView date_added =(TextView) view.findViewById(R.id.etCoDate);
        ConditionList symptoms=  conditionLists.get(position);
        condition_name.setText(symptoms.getCondition_name());
        date_added.setText(symptoms.getDate_added());
        return super.getView(position, convertView, parent);
    }
}
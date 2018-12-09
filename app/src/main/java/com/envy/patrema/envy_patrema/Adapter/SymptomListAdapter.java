package com.envy.patrema.envy_patrema.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.SymptomList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;

public class SymptomListAdapter extends ArrayAdapter<SymptomList> {

    private List<SymptomList> symptomsList;
    private Context mCtx;

    public SymptomListAdapter(List<SymptomList> S,Context c){
        super(c, R.layout.symptom_list,S);
        this.symptomsList=S;
        this.mCtx=c;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(mCtx);
        View view= inflater.inflate(R.layout.symptom_list,null,true);
        TextView symptom_name =(TextView) view.findViewById(R.id.etName);
        TextView date_added =(TextView) view.findViewById(R.id.etVisDate);
        SymptomList symptoms=  symptomsList.get(position);
        symptom_name.setText(symptoms.getSymptom_name());
        date_added.setText(symptoms.getDate_added());
        return  view;
    }
}
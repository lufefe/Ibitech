package com.envy.patrema.envy_patrema.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.MySymptomsList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;

public class SymptomListAdapter extends ArrayAdapter<MySymptomsList> {

    private List<MySymptomsList> symptomsList;
    private Activity context;

    public SymptomListAdapter(List<MySymptomsList> symptomsList, Activity context){
        super(context, R.layout.my_symptoms_list, symptomsList);

        this.symptomsList = symptomsList;
        this.context = context;
    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder;

        if (r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.my_symptoms_list, null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.tvSymptom.setText(symptomsList.get(position).getSymptom());
        viewHolder.tvDateAdded.setText(symptomsList.get(position).getDate_added());
        viewHolder.tvSeverity.setText(symptomsList.get(position).getSeverity());

        return  r;
    }

    class ViewHolder{
        TextView tvSymptom, tvDateAdded, tvSeverity;

        ViewHolder(View v){
            tvSymptom = v.findViewById(R.id.etSymptom);
            tvDateAdded = v.findViewById(R.id.etDateAdded);
            tvSeverity = v.findViewById(R.id.etSeverity);
        }
    }
}
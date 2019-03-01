package com.envy.patrema.envy_patrema.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.MyAllergiesList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;

public class AllergyListAdapter extends ArrayAdapter<MyAllergiesList> {

    private List<MyAllergiesList> allergyList;
    private Activity context;

    public AllergyListAdapter(List<MyAllergiesList> allergyList, Activity context) {
        super(context, R.layout.my_allergy_list, allergyList);

        this.allergyList = allergyList;
        this.context = context;

    }


    @Override
    public View getView(int position,  View convertView,  ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder;

        if (r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.my_allergy_list, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.tvAllergy.setText(allergyList.get(position).getAllergy());
        viewHolder.tvDateAdded.setText(allergyList.get(position).getDate_added());
        viewHolder.tvTested.setText(allergyList.get(position).getTested());;

        return r;
    }

    class ViewHolder{
        TextView tvAllergy, tvDateAdded, tvTested;

        ViewHolder(View v){
            tvAllergy = v.findViewById(R.id.etAllergy);
            tvDateAdded = v.findViewById(R.id.etDateAdded);
            tvTested = v.findViewById(R.id.etTested);

        }

    }
}
package com.envy.patrema.envy_patrema.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.CreateVisitList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreatePatientVisitsAdapter extends ArrayAdapter<CreateVisitList> {

    private List<CreateVisitList> visitList;
    private Activity context;

    public CreatePatientVisitsAdapter(List<CreateVisitList> visitList, Activity context) {
        super(context, R.layout.custom_create_visit_row, visitList);

        this.visitList = visitList;
        this.context = context;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder;

        if (r == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.custom_create_visit_row, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.txtName.setText(String.format("%s %s", visitList.get(position).getName(), visitList.get(position).getSurname()));
        viewHolder.txtID.setText(visitList.get(position).getIdNo());
        //viewHolder.imgPatientPic.

        return r;
    }

    class ViewHolder {
        TextView txtName, txtID;
        CircleImageView imgPatientPic;

        ViewHolder(View v) {
            txtName = v.findViewById(R.id.txtName);
            txtID = v.findViewById(R.id.txtIDNo);
            imgPatientPic = v.findViewById(R.id.imgProfilePic);
        }
    }
}

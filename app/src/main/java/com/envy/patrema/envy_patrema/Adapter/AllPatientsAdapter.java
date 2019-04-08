package com.envy.patrema.envy_patrema.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.AllPatientsList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllPatientsAdapter extends ArrayAdapter<AllPatientsList> {

    private List<AllPatientsList> allPatientsLists;
    private Activity context;

    public AllPatientsAdapter(List<AllPatientsList> allPatientsLists,Activity context) {
        super(context, R.layout.custom_allpatients_row, allPatientsLists);

        this.allPatientsLists = allPatientsLists;
        this.context = context;

    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder;

        if (r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.custom_allpatients_row,null,true );
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }

        viewHolder.txtName.setText(String.format("%s %s", allPatientsLists.get(position).getName(), allPatientsLists.get(position).getSurname()));
        viewHolder.txtCellNo.setText(allPatientsLists.get(position).getCellNo());
        viewHolder.txtDOB.setText(allPatientsLists.get(position).getDob());
        //viewHolder.imgPatientPic.setImageBitmap();


        return r;
    }

    class ViewHolder{
        TextView txtName, txtCellNo, txtDOB;
        CircleImageView imgPatientPic;

        ViewHolder(View v){
            txtName = v.findViewById(R.id.tvFullname);
            txtCellNo = v.findViewById(R.id.tvCellNo);
            txtDOB = v.findViewById(R.id.tvDOB);
            imgPatientPic = v.findViewById(R.id.imgProfilePic);
        }

    }

}

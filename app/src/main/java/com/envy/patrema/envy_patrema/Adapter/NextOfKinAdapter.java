package com.envy.patrema.envy_patrema.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.NextOfKinList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;

public class NextOfKinAdapter extends ArrayAdapter<NextOfKinList> {

    private List<NextOfKinList> nextOfKinLists;
    private Context mCtx;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_next_of_kin_row, parent, false);

        NextOfKinList nextOfKinList = nextOfKinLists.get(position);
        TextView name = customView.findViewById(R.id.tvFullname);
        TextView cell = customView.findViewById(R.id.tvCellNo);
        TextView relation = customView.findViewById(R.id.tvRelation);

        name.setText(String.format("%s %s", nextOfKinList.getName(), nextOfKinList.getSurname()));
        cell.setText(nextOfKinList.getCellNo());
        relation.setText(nextOfKinList.getRelation());
        return customView;
    }

    public NextOfKinAdapter( List<NextOfKinList> nextOfKinLists, Context context) {
        super(context, R.layout.custom_next_of_kin_row,nextOfKinLists);
        this.nextOfKinLists = nextOfKinLists;
        this.mCtx = context;
    }
}

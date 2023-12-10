package com.example.pureflow_group_project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ListAdapter extends ArrayAdapter {

    private Activity mContext;
    // Create a list of reservoirs
    List<Reservoirs> reservoirsList;

    // Constructor
    public ListAdapter(Activity mContext, List<Reservoirs> reservoirsList) {
        super(mContext, R.layout.list_item, reservoirsList);
        this.mContext = mContext;
        this.reservoirsList = reservoirsList;
    }

    // Get the view of the list
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = mContext.getLayoutInflater();
        View listItemView=layoutInflater.inflate(R.layout.list_item,null,true);

        TextView name = listItemView.findViewById(R.id.name);
        TextView lat = listItemView.findViewById(R.id.lat);
        TextView lon = listItemView.findViewById(R.id.lon);
        TextView lvl = listItemView.findViewById(R.id.lvl);

        Reservoirs reservoirs = reservoirsList.get(position);

        name.setText(reservoirs.getName());
        lat.setText(String.valueOf(reservoirs.getLat()));
        lon.setText(String.valueOf(reservoirs.getLon()));
        lvl.setText(String.valueOf(reservoirs.getLvl()));

        return listItemView;
    }
}

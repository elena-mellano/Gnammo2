package com.elena.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by elena on 22/05/14.
 */

public class AdapterList extends ArrayAdapter<Event> {

    public AdapterList(Context context, int textViewResourceId,
                            Event[] objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row, null);
        }

        TextView title  = (TextView)convertView.findViewById(R.id.text);
        TextView owner  = (TextView)convertView.findViewById(R.id.textowner);
        ImageView imag  = (ImageView)convertView.findViewById(R.id.image);
        Event single = getItem(position);
        title.setText(single.getTitle());
        owner.setText(single.getOwner().getName());
        imag.setImageBitmap(single.getImg());

        return convertView;
    }

}
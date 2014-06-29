package com.elena.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by elena on 22/05/14.
 */

public class AdapterList extends ArrayAdapter<Event> {

    public AdapterList(Context context, int textViewResourceId,
                            Event[] objects) {
        super(context, textViewResourceId, objects);
    }
    Bitmap img;

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
        Bitmap bitmap=null;
        String url=single.getUrlImg();
        img=null;
        try {
            if (url.compareTo("")!=0){
                URL urleff = null;
                try {
                    urleff = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 6;
                bitmap = BitmapFactory.decodeStream(urleff.openStream(), null, options);
                img = bitmap;
            }
        }catch (IOException e2){
            e2.printStackTrace();
        }



        imag.setImageBitmap(img);

        return convertView;
    }

    public Bitmap getImage(){
        return(img);
    }

}
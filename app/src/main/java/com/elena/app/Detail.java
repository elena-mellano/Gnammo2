package com.elena.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by elena on 01/06/14.
 */
public class Detail extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        Event e=(Event)getIntent().getSerializableExtra("event");
        TextView t = (TextView)findViewById(R.id.textView);
        t.setText(e.getTitle());
        TextView t1 = (TextView)findViewById(R.id.textView2);
        t1.setText(e.getLocation().getCity());
        TextView t2 = (TextView)findViewById(R.id.textView3);
        t2.setText(String.valueOf(e.getPrice()));
        TextView t3 = (TextView)findViewById(R.id.textView4);
        t3.setText(e.getDates().getDay());
        String url=e.getUrlImg();
        Bitmap bitmap=null;
        try {
            if (url.compareTo("")!=0){
                URL urleff = null;
                try {
                    urleff = new URL(url);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                //BitmapFactory.Options options=new BitmapFactory.Options();
              //  options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeStream(urleff.openStream());
                ImageView imag  = (ImageView)findViewById(R.id.imageView);

                imag.setImageBitmap(bitmap);
            }
        }catch (IOException e2){
            e2.printStackTrace();
        }



    }
}

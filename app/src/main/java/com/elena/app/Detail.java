package com.elena.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by elena on 01/06/14.
 */
public class Detail extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        /* retrieving all the informations in order to display them */

        Button b1 = (Button)findViewById(R.id.button);
        b1.setText(getResources().getString(R.string.there_are) +
                " " + getIntent().getExtras().getInt("seats_avaible") +
                " " + getResources().getString(R.string.seat) );

        TextView t5 = (TextView)findViewById(R.id.textView6);
        t5.setText(getIntent().getExtras().getString("title"));
        TextView t1 = (TextView)findViewById(R.id.textView2);
        t1.setText(getIntent().getExtras().getString("location"));
        TextView t2 = (TextView)findViewById(R.id.textView3);
        t2.setText(String.valueOf(getIntent().getExtras().getDouble("price")) +
                " " + getResources().getString(R.string.euro));
        TextView t3 = (TextView)findViewById(R.id.textView4);
        t3.setText(getIntent().getExtras().getString("date_start"));
        TextView t4 = (TextView)findViewById(R.id.textView5);
        t4.setText(getIntent().getExtras().getString("date_end"));
        TextView t6 = (TextView)findViewById(R.id.textView7);
        t6.setText(Html.fromHtml(getIntent().getExtras().getString("event_description")));
        TextView t7 = (TextView)findViewById(R.id.textView);
        t7.setText(getIntent().getExtras().getString("title_menu"));
        TextView t8 = (TextView)findViewById(R.id.textView8);
        t8.setText(Html.fromHtml(getIntent().getExtras().getString("description_menu")));
        TextView t9 = (TextView)findViewById(R.id.textView9);
        t9.setText(getIntent().getExtras().getString("owner"));
        ImageView imag  = (ImageView)findViewById(R.id.imageView);
        imag.setImageBitmap((Bitmap) getIntent().getParcelableExtra("single_event_bitmap"));
        String url= getIntent().getExtras().getString("owner_img");

        Bitmap bitmap2=null;
        try {
            if (url.compareTo("")!=0){
                URL urleff = null;
                try {
                    urleff = new URL(url);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                BitmapFactory.Options options2=new BitmapFactory.Options();
                options2.inSampleSize = 8;
                bitmap2 = BitmapFactory.decodeStream(urleff.openStream(), null, options2);
                ImageView img  = (ImageView)findViewById(R.id.imageView2);

                img.setImageBitmap(bitmap2);
            }
        }catch (IOException e2){
            e2.printStackTrace();
        }
    }

    public void onClick(View vo){
        Intent start=new Intent(Detail.this, Login.class);
         /* passing all the informations to the next activity in order to display */

        start.putExtra("title", getIntent().getExtras().getString("title"));
        start.putExtra("price", getIntent().getExtras().getDouble("price"));
        start.putExtra("event_description", getIntent().getExtras().getString("event_description"));

        startActivity(start);
    }
}

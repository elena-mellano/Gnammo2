package com.elena.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by elena on 27/05/14.
 */
public class ListEvent {
    private String title;
    private String  owner;
    private Bitmap img;

    public void setTitle(String t){title=t;return;}
    public void setOwner(String o){owner=o;return;}
    public void setImg(String url){
        Bitmap bitmap=null;
        try {
            if (url.compareTo("")!=0){
                URL urleff = null;
                try {
                    urleff = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inSampleSize = 8;
                bitmap = BitmapFactory.decodeStream(urleff.openStream(), null, options);
                img = bitmap;
         }
      }catch (IOException e2){
            e2.printStackTrace();
        }
    }

    public String getTitle(){return title;}
    public String getOwner(){return owner;}
    public Bitmap getImg(){return img;}
}

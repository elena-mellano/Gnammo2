package com.elena.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by elena on 21/05/14.
 */
public class Event implements Serializable {

    private String status;

    public  String getStatus(){return status;}
    public void setStatus(String s){status=s;}

    private String  description;

    public  String getDescription(){return description;}
    public void setDescription(String d){description=d;}

    private double price;

    public  double getPrice(){return price;}
    public void setPrice(double p){price=p;}

    private Book[][] bookings;

    public  Book[][] getBookings(){return bookings;}
    public void setBookings(Book[][] b){bookings=b;}

    private Seat seats;

    public  Seat getSeats(){return seats;}
    public void setSeats(Seat s){seats=s;}

    private Person  owner;

    public void setOwner(Person p){owner=p;}

    private Album  album;

    public  Album getAlbum(){return album;}
    public void setAlbum(Album a){album=a;}

    private Dates dates;

    public  Dates getDates(){return dates;}
    public void setDates(Dates s){dates=s;}

    private String title;

    public String getTitle(){return title;}
    public void setTitle(String t ){title=t;}

    private MenuEv menu;

    public  MenuEv getMenu(){return menu;}
    public void setMenu(MenuEv s){menu=s;}

    private int  verbosity;

    public  int getVerbosity(){return verbosity;}
    public void setVerbosity(int s){verbosity=s;}

    private Location location;

    public  Location getLocation(){return location;}
    public void setLocation(Location s){location=s;}
    private Rel rel;

    private Bitmap img;

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
    public Bitmap getImg(){return img;}


    public String getUrlImg(){return album.getUrlImg();}
    public String getOwner2(){return owner.getName();}
    public Person getOwner(){return owner;}

}

package com.elena.app;

import java.io.Serializable;

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




    public String getUrlImg(){return album.getUrlImg();}
    public String getOwner2(){return owner.getName();}
    public Person getOwner(){return owner;}

}

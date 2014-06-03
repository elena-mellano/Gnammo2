package com.elena.app;

/**
 * Created by elena on 21/05/14.
 */
public class Event_Vector {
    private int status;
    private String message;
    private Data data;

    public int getStatus(){return status;}
    public void setStatus(int s){this.status=s;}
    public Event getData(int i){return data.getEvent(i);}
    public int length(){return data.length();}
    public String [] getArrayTitle(){return data.getArrayTitle();}
    public String [] getArrayImg(){return data.getArrayImg();}
    public String [] getArrayOwner(){return data.getArrayOwner();}
    public Event[] getEvents(){return data.getEvents();}
}

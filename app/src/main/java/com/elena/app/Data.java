package com.elena.app;

/**
 * Created by elena on 21/05/14.
 */
public class Data {
    private Event[] events;

    public Event getEvent(int i){return events[i];}
    public int length(){return events.length;}
    public String[] getArrayTitle(){
        String [] array = new String[events.length];
        for (int i=0; i<events.length; i++){
            array[i]=events[i].getTitle();
        }
        return array;
    }
    public String[] getArrayImg(){
        String [] array = new String[events.length];
        for (int i=0; i<events.length; i++){
            array[i]=events[i].getUrlImg();
        }
        return array;
    }
    public String[] getArrayOwner(){
        String [] array = new String[events.length];
        for (int i=0; i<events.length; i++){
            array[i]=events[i].getOwner2();
        }
        return array;
    }
    public Event[] getEvents(){return this.events;}
    }



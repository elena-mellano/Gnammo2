package com.elena.app;
import java.io.Serializable;
/**
 * Created by elena on 21/05/14.
 */
public class Day implements Serializable {
    private int hour;
    private int month;
    private String iso;
    private int year;
    private int day;
    private int minute;

    public String getDay(){
        switch (month){
            case 1:
                return (day + " Gennaio");
            case 2:
                return (day + " Febbraio");
            case 3:
                return (day + " Marzo");
            case 4:
                return (day + " Aprile");
            case 5:
                return (day + " Maggio");
            case 6:
                return (day + " Giugno");
            case 7:
                return (day + " Luglio");
            case 8:
                return (day + " Agosto");
            case 9:
                return (day + " Settembre");
            case 10:
                return (day + " Ottobre");
            case 11:
                return (day + " Novembre");
            case 12:
                return (day + " Dicembre");

        }
        return ("Evento senza data");
    }
}

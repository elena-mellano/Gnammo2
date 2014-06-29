package com.elena.app;
import android.content.Context;

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

    public String getDay(Context c) {
        switch (month) {
            case 1:
                return (day + " " + c.getString(R.string.January));
            case 2:
                return (day + " " + c.getString(R.string.February));
            case 3:
                return (day + " " + c.getString(R.string.March));
            case 4:
                return (day + " " + c.getString(R.string.April));
            case 5:
                return (day + " " + c.getString(R.string.May));
            case 6:
                return (day + " " + c.getString(R.string.June));
            case 7:
                return (day + " " + c.getString(R.string.July));
            case 8:
                return (day + " " + c.getString(R.string.August));
            case 9:
                return (day + " " + c.getString(R.string.September));
            case 10:
                return (day + " " + c.getString(R.string.October));
            case 11:
                return (day + " " + c.getString(R.string.November));
            case 12:
                return (day + " " + c.getString(R.string.December));

        }
        return ("No data");
    }
}

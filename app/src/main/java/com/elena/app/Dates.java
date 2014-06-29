package com.elena.app;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by elena on 21/05/14.
 */
public class Dates implements Serializable {
    private Day ends_subscription;
    private Day takes_place;
    public String getDay(Context c){return takes_place.getDay(c);};
    public String getends(Context c ){return ends_subscription.getDay(c);};
}

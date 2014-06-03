package com.elena.app;

import java.io.Serializable;

/**
 * Created by elena on 21/05/14.
 */
public class Dates implements Serializable {
    private Day ends_subscription;
    private Day takes_place;
    public String getDay(){return takes_place.getDay();};
}

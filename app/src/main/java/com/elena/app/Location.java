package com.elena.app;

import java.io.Serializable;

/**
 * Created by elena on 21/05/14.
 */

public class Location implements Serializable {
    private String geo_lng;
    private String        city;
    private String        geo_lat;
    private String        telephone_no;
    private String        address;
    private int      pk;
    private String      title;
    private String        email;
    private Viewport       viewport;

    public String getCity(){return city;}
    public Viewport getViewport() {return viewport;}
}

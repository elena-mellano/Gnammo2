package com.elena.app;

import java.io.Serializable;

/**
 * Created by elena on 21/05/14.
 */
public class Seat implements Serializable {
    private int available;
    private int max;
    private int reserved;
    private int min;

    public int getAvailable(){return available;}
}

package com.elena.app;

import java.io.Serializable;

/**
 * Created by elena on 21/05/14.
 */
public class Viewport implements Serializable {
   Position northeast;
    Position southwest;

    public Position getPosition() {return northeast;}

}

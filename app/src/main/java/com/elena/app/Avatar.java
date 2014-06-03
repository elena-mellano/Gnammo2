package com.elena.app;

import java.io.Serializable;

/**
 * Created by elena on 21/05/14.
 */
public class Avatar implements Serializable {
    private String image;
    private String type;
    public String getUrlImg(){return image;}
}

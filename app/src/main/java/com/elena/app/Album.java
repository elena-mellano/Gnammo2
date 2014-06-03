package com.elena.app;

import java.io.Serializable;

/**
 * Created by elena on 21/05/14.
 */
public class Album implements Serializable {
    private String absolute_url;
     private String album_cover_photo;
     private Photo[] photos;
     private String title;

    public String getUrlImg(){return album_cover_photo;}
}

package com.elena.app;

import java.io.Serializable;

/**
 * Created by elena on 21/05/14.
 */
public class Person implements Serializable {
    private String username;
    private String last_name;
    private String blog_url;
    private String long_description;
    private String first_name;
    private String site_url;
    private boolean is_cook;
    private Avatar avatar;
    private Rel rel;
    private String short_description;
    private String pk;

    public String getUrlImg(){return avatar.getUrlImg();}
    public String getName(){return username;}
}

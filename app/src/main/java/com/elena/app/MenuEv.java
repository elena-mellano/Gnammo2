package com.elena.app;

import java.io.Serializable;

/**
 * Created by elena on 21/05/14.
 */
public class MenuEv implements Serializable {
    private String description;
    private String title;

    public String getTitle() {return title;};
    public String getDescription() {return description;};
}
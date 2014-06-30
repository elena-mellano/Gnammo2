package com.elena.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class All_events extends FragmentActivity implements ActionBar.TabListener {

    ActionBar actionBar;
    ViewPager viewPager;
    Event [] ev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        Boolean errors = false;

        /* GET and POST initialization */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /* Reading data from JSON, putting results into array ev */
        ev = null;

        DefaultHttpClient httpClient = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();


        HttpGet getMethod = new HttpGet("http://gnammo.com/api/2/events");


        //getMethod.setHeader("Content-type", "application/json")
        Gson gson = new Gson();
        Event_Vector e = null;
        try {
            HttpResponse response = httpClient.execute(getMethod);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            e = gson.fromJson(reader, Event_Vector.class);
            ev = e.getEvents();
        } catch (IOException e1) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle(R.string.no_internet);

            // set dialog message
            alertDialogBuilder
                    .setMessage(R.string.activated_internet)
                    .setCancelable(false)
                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            All_events.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.reload, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            finish();
                            startActivity(getIntent());
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            errors = true;

        }

        // Now we got all the data into ev array

        if (errors == false) {
        /* IMPLEMENTING TABS */
            // get the ViewPager handle
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager(), ev));
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            // create the actionBar and sets navigation mode to TABS
            actionBar = getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

            // create TAB 1
            ActionBar.Tab list_tab = actionBar.newTab();
            list_tab.setText(R.string.event_list);
            list_tab.setTabListener(this);

            // create TAB 2
            ActionBar.Tab map_tab = actionBar.newTab();
            map_tab.setText(R.string.event_map);
            map_tab.setTabListener(this);

            // add TABS to actionBar
            actionBar.addTab(list_tab);
            actionBar.addTab(map_tab);
        }
    }

    /* Option menu */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_events, menu);
        return true;
    }

    /*
        OnTab listeners, used for changing tabs
     */

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
}

    /*
        Implementing pageAdapter, used to change tabs
    */

class MyPageAdapter extends FragmentPagerAdapter {

    Event[] ev;
    Bundle bundle = null;

    MyPageAdapter(FragmentManager fm, Event[] event_list) {
        super(fm);
        ev = event_list;
        bundle = new Bundle();
        bundle.putSerializable("event_list", ev);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {

            case 0: fragment = new EventListFragment();
                    // passing event_list vector to fragment through Bundle
                    fragment.setArguments(bundle);

                break;

            case 1: fragment = new EventMapFragment();
                    // passing event_list vector to fragment through Bundle
                    fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

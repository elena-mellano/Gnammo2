package com.elena.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class All_events extends ActionBarActivity {
    ArrayList<ListEvent> array = null;
    AdapterList adapter=null;
    ListView listView;
    Event [] ev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }




        listView = (ListView)findViewById(R.id.listView);
        array = new ArrayList();
        adapter = new AdapterList(this, R.layout.row, array);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id){
                Intent start=new Intent(All_events.this, Detail.class);
                Event e1 = ev[pos];

                start.putExtra("event", e1);

                startActivity(start);


            }
        });

        ev = null;

            DefaultHttpClient httpClient = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpGet getMethod = new HttpGet("http://staging.gnammo.com/api/2/events");
            //getMethod.setHeader("Content-type", "application/json")
            Gson gson =new Gson();
            Event_Vector e=null;
            try{
                HttpResponse response = httpClient.execute(getMethod);
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                e = gson.fromJson(reader, Event_Vector.class);

            }catch(IOException e1){

            }
            ev = e.getEvents();


        int j;

        for (j=0;j<ev.length;j++){
            try {
            new GnammoSync().execute(ev[j]);
            } catch(Exception err) {}

        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_events, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_all_events, container, false);
            return rootView;
        }
    }

    private class GnammoSync extends AsyncTask<Event, Integer, Integer> {

        protected Integer doInBackground(Event... evs) {
            ListEvent tmp = new ListEvent();
            tmp.setTitle(evs[0].getTitle());
            tmp.setOwner(evs[0].getOwner2());
            tmp.setImg(evs[0].getUrlImg());
            array.add(tmp);
            return 0;
        }



        protected void onProgressUpdate(Integer... progress) {

        }

        protected void onPostExecute(Integer i) {
            adapter.notifyDataSetChanged();
            //this.cancel(true);
        }

    }
}

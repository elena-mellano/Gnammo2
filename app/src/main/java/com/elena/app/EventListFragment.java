package com.elena.app;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EventListFragment extends Fragment {

    ListView listView;
    AdapterList adapter=null;
    Event[] ev = null;
    // array filled in background utilized to pass to adapter
    ArrayList<Event> background_array = null;

    public EventListFragment() {
        // Required empty public constructor
    }


    @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Retrieve the event_list from activity
        ev = (Event[]) getArguments().getSerializable("event_list");

         // Inflate the layout for this fragment
        View fragment_list=inflater.inflate(R.layout.fragment_event_list, container, false);


        return fragment_list;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // getting listView handler to fill with informations
        listView = (ListView) getActivity().findViewById(R.id.listView);
        //setting the adapter to the ListView
        adapter = new AdapterList(getActivity(), R.layout.row, ev );
        listView.setAdapter(adapter);

        // implementing the onClick listener to the listView
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id){
                Intent start=new Intent(getActivity() , Detail.class);

                /* passing all the informations to the next activity in order to display */
                start.putExtra("seats_avaible", ev[pos].getSeats().getAvailable());
                start.putExtra("title", ev[pos].getTitle());
                start.putExtra("location", ev[pos].getLocation().getCity());
                start.putExtra("price", ev[pos].getPrice());
                start.putExtra("date_start", ev[pos].getDates().getDay(getActivity()));
                start.putExtra("date_end", ev[pos].getDates().getends(getActivity()));
                start.putExtra("event_description", ev[pos].getDescription());
                start.putExtra("title_menu", ev[pos].getMenu().getTitle());
                start.putExtra("description_menu", ev[pos].getMenu().getDescription());
                start.putExtra("owner", ev[pos].getOwner().getName());
                start.putExtra("owner_img", ev[pos].getOwner().getUrlImg());
                ImageView imageView = (ImageView)componente.findViewById(R.id.image);

                BitmapDrawable b =(BitmapDrawable)imageView.getDrawable();
                Bitmap bitmap = b.getBitmap();
                start.putExtra("single_event_bitmap",bitmap) ;

                startActivity(start);



            }
        });


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}

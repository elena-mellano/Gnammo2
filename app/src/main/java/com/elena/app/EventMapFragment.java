package com.elena.app;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EventMapFragment extends Fragment {


    public EventMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Retrieve the event_list from activity
        Event[] ev = (Event[]) getArguments().getSerializable("event_list");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_map, container, false);
    }


}

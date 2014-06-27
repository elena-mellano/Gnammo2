package com.elena.app;

import android.location.*;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;


public class EventMapFragment extends Fragment
                                implements  View.OnClickListener {

    private GoogleMap mMap;
    private double radius = 3.0;
    private TextView radiusView;
    Event[] ev = null;
    ArrayList<Marker> markerList = null;
    LocationManager locationManager;

    public EventMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Retrieve the event_list from activity
        ev = (Event[]) getArguments().getSerializable("event_list");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_map, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* set maps */
        setUpMap();

        /*** Initializations ***/
        if ( savedInstanceState != null ) {
            // Set the previous value of radius
            radius = savedInstanceState.getDouble("Radius");
            // Set the previous camera position
            CameraPosition cp = savedInstanceState.getParcelable("CameraPosition");
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        }
        else {
            // set the view to Granada
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.179373, -3.600186), 13));
        }

        // get system suppot for GPS
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        // handle buttons
        Button increase = (Button) getActivity().findViewById(R.id.increase);
        Button decrease = (Button) getActivity().findViewById(R.id.decrease);
        increase.setOnClickListener(this);
        decrease.setOnClickListener(this);

        /* Set the view of the radius indicator (top left) */
        radiusView = (TextView) getActivity().findViewById(R.id.radiusView);
        radiusView.setText(String.valueOf(radius));

        Viewport viewPort;
        markerList = new ArrayList<Marker>();
        double lat, lng;

        /* adding all the markers in the map */
        for ( int i=0; i < ev.length; i++ ) {
            // getting position of event ev[i]
            viewPort = ev[i].getLocation().getViewport();
            if ( viewPort.getPosition() != null ) {
                lat = viewPort.getPosition().getLat();
                lng = viewPort.getPosition().getLng();
                // creating new marker
                Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(ev[i].getTitle()));
                // add new marker to marker vector
                markerList.add(marker);
            }
        }

        setMarkersInRadius(radius);
    }

    private void setUpMap() {
        if (mMap == null) {
            // the map is not set yet, let's set it now
            mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            // add the My Location Button
            mMap.setMyLocationEnabled(true);
        }
    }

    private void setMarkersInRadius(double radius) {
        if (mMap != null) {
            // --if map has been set, let's set up the marker looking by radius--

            // getting my actual latitude and longitude
            Location myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            /* for every marker, calculate distance from user and decide whether to show it or not */
            for (int i=0; i < markerList.size(); i++) {
                Marker marker = markerList.get(i);

                // getting latitude and longitude of marker
                double markerLat = marker.getPosition().latitude;
                double markerLng = marker.getPosition().longitude;

                // creating a Location object with marker latitude and longitude
                Location markerLocation = new Location("start");
                markerLocation.setLatitude(markerLat);
                markerLocation.setLongitude(markerLng);

                // calculating distance
                float distance = markerLocation.distanceTo(myLocation);

                // setting marker visible or not depending on radius value
                marker.setVisible( distance <= radius*1000 );
            }

        }
    }

    /* used to save radius variable in order to restore it after orientation change */
    public void onSaveInstanceState( Bundle outBundle) {
        // saving radius
        outBundle.putDouble("Radius", radius);
        // saving camera position
        outBundle.putParcelable("CameraPosition", mMap.getCameraPosition());
    }

    /* capturing onClick on the increase and decrease buttons */
    @Override
    public void onClick(View view) {
        switch( view.getId()) {
            case R.id.increase:
                            if ( radius < 150 ) {
                                radius += 0.5;
                                radiusView.setText(String.valueOf(radius));
                            }
                            setMarkersInRadius(radius);
                break;
            case R.id.decrease:
                            if ( radius > 0.5 ) {
                                radius -= 0.5;
                                radiusView.setText(String.valueOf(radius));
                            }
                            setMarkersInRadius(radius);
                break;
        }
    }

}

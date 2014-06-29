package com.elena.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class EventMapFragment extends Fragment
        implements  View.OnClickListener {

    private GoogleMap mMap;
    private double radius = 3.0;
    private TextView radiusView;
    Event[] ev = null;
    ArrayList<Marker> markerList = null;
    LocationManager locationManager;
    Circle circle;

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

        // get system suppot for GPS
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        // getting my actual latitude and longitude
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        // setting default view on my position
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 13));

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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                int pos=markerList.indexOf(marker);
                Intent start = new Intent(getActivity(), Detail.class);

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
                Bitmap bitmap=null;
                String url=ev[pos].getUrlImg();
                Bitmap img=null;
                try {
                    if (url.compareTo("")!=0){
                        URL urleff = null;
                        try {
                            urleff = new URL(url);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        BitmapFactory.Options options=new BitmapFactory.Options();
                        options.inSampleSize = 4;
                        bitmap = BitmapFactory.decodeStream(urleff.openStream(), null, options);
                        img = bitmap;
                    }
                }catch (IOException e2){
                    e2.printStackTrace();
                }
                start.putExtra("single_event_bitmap",bitmap);
                startActivity(start);


                return false;
            }
        });

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

            // setting the circle around my position with radius size
            if (circle != null) { circle.remove(); }

            circle = mMap.addCircle( new CircleOptions()
                    .center(new LatLng(myLocation.getLatitude(), myLocation.getLongitude() ))
                    .radius(radius*1000)
                    .strokeWidth(2)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.parseColor("#500084d3")));

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

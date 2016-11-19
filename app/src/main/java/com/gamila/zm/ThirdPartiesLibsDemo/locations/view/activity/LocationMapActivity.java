package com.gamila.zm.ThirdPartiesLibsDemo.locations.view.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.gamila.zm.ThirdPartiesLibsDemo.AppConstants;
import com.gamila.zm.ThirdPartiesLibsDemo.locations.model.dao.Location;
import com.gamila.zm.ThirdPartiesLibsDemo.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubeStandalonePlayer;

/**
 * Created by zeinab on 4/30/2016.
 */
public class LocationMapActivity extends AppCompatActivity implements OnMapReadyCallback{

    public static final String LOCATION_ITEM_KEY = "locationItem";
    private GoogleMap mMap;
    private Location locationItem;
    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_map);

        if(getIntent().getExtras() != null){
            locationItem = (Location) getIntent().getSerializableExtra(LOCATION_ITEM_KEY);
        }
         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in city , and move the camera.
        LatLng location = new LatLng(Double.parseDouble(locationItem.getLat()),
                Double.parseDouble(locationItem.getLog()));
        mMap.addMarker(new MarkerOptions().position(location)
                .title("Happy in "+locationItem.getCity() )
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                startYoutubeActivity();
            }
        });


    }

    private void startYoutubeActivity() {

        try {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(this, AppConstants.APP_DEVELOPER_KEY, locationItem.getVideoId());
            startActivity(intent);
        }catch (ActivityNotFoundException  ex){
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + locationItem.getVideoId()));
            startActivity(i);
        }
    }
}

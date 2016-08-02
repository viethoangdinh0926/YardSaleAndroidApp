package com.viet.yardsale;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.viet.yardsale.search_yardsale_operations.YardSalePreviewActivity;
import com.viet.yardsale.services.MarkerLabelAdapter;
import com.viet.yardsale.services.StaticComponents;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        handleNewLocation(StaticComponents.latitude, StaticComponents.longtitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        handleNewLocation(StaticComponents.latitude, StaticComponents.longtitude);
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void handleNewLocation(double latitude, double longtitude) {
        SimpleDateFormat curFormater = new SimpleDateFormat("MM/dd/yyyy");
        String[] lines = StaticComponents.returned_yard_sales_information.split("\\|");

        for(String line: lines){
            String[] subLines = line.split("\\s+");//space between 2 numbers
            LatLng latLng = new LatLng(Double.parseDouble(subLines[0]),Double.parseDouble(subLines[1]));
            String closeTime = (Math.round((Long.valueOf(subLines[2]) % 1440)/60)-12)+".PM";
            long openTime = (Long.valueOf(subLines[2]))*60000;
            Date date = new Date();
            date.setTime(openTime);
            subLines[2] = curFormater.format(date);

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(subLines[2]+" "+subLines[3]+" "+subLines[4]+" "+subLines[5]+" "+subLines[6]+" "+subLines[7]+" "+closeTime+" "+subLines[0]+" "+subLines[1]);//user names of detected locations

            mMap.addMarker(options);
        }

        LatLng latLng = new LatLng(latitude, longtitude);

        MarkerOptions options = new MarkerOptions()
                .flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.yourlocation))
                .anchor(0.5f, 0.5f)
                .position(latLng)
                .title("You are here!");
        mMap.addMarker(options);

        mMap.setInfoWindowAdapter(new MarkerLabelAdapter(getLayoutInflater()));
        mMap.setOnInfoWindowClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12F));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if(!StaticComponents.number_of_yard_sale_photos.equals("0")){
            Intent intent = new Intent(this, YardSalePreviewActivity.class);
            startActivity(intent);
        }
        //Toast.makeText(this, marker.getTitle(), Toast.LENGTH_LONG).show();
    }

}

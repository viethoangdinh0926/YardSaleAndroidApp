package com.viet.yardsale.services;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.viet.yardsale.search_yardsale_operations.SearchYardSalesActivity;
import com.viet.yardsale.post_yardsale_operations.PostYardSaleMainActivity;
import com.viet.yardsale.R;

import java.util.List;

/**
 * Created by Viet on 6/5/2015.
 */
public class LocatingOperations implements/*GoogleApiClient.ConnectionCallbacks,*//* GoogleApiClient.OnConnectionFailedListener,*/ com.google.android.gms.location.LocationListener{

    private LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;

    private Activity activity;
    public String latitude = null;
    public String longtitude = null;

    //public static final String TAG = MapsActivity.class.getSimpleName();

    public LocatingOperations(Activity activity){
        this.activity = activity;

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                //.addConnectionCallbacks(this)
                //.addOnConnectionFailedListener(this)
                .addApi(com.google.android.gms.location.LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(activity);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    public void updateLocation(){
        if(UserPhoneServices.isInternetConnectionOn(activity)) {
            if (activity instanceof PostYardSaleMainActivity) {
                if (((PostYardSaleMainActivity) activity).useCurrentLocation) {
                    if (mGoogleApiClient.isConnected()) {
                        Location location = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (location != null) {
                            latitude = String.valueOf(location.getLatitude());
                            longtitude = String.valueOf(location.getLongitude());
                        } else {
                            com.google.android.gms.location.LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                        }
                    } else {
                        ((TextView) ((PostYardSaleMainActivity) activity).findViewById(R.id.errMessage)).setText("GoogleAPIClient is not connected.");
                    }
                } else {
                    if (!((EditText) ((PostYardSaleMainActivity) activity).findViewById(R.id.address)).getText().toString().equals("")) {
                        LatLng location = getLocationFromAddress(((EditText) ((PostYardSaleMainActivity) activity).findViewById(R.id.address)).getText().toString());
                        if (location == null) {
                            ((TextView) ((PostYardSaleMainActivity) activity).findViewById(R.id.errMessage)).setText("The address does not exist");
                        } else {
                            latitude = String.valueOf(location.latitude);
                            longtitude = String.valueOf(location.longitude);
                        }
                    } else {
                        ((TextView) ((PostYardSaleMainActivity) activity).findViewById(R.id.errMessage)).setText("Please enter the address.");
                    }
                }
            } else {
                if (((SearchYardSalesActivity) activity).useCurrentLocation) {
                    if (mGoogleApiClient.isConnected()) {
                        Location location = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        if (location != null) {
                            latitude = String.valueOf(location.getLatitude());
                            longtitude = String.valueOf(location.getLongitude());
                        } else {
                            com.google.android.gms.location.LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                        }
                    } else {
                        ((TextView) ((SearchYardSalesActivity) activity).findViewById(R.id.errMessage)).setText("GoogleAPIClient is not connected.");
                        latitude = null;
                        longtitude = null;
                    }
                } else {
                    if (!((EditText) ((SearchYardSalesActivity) activity).findViewById(R.id.address)).getText().toString().equals("")) {
                        LatLng location = getLocationFromAddress(((EditText) ((SearchYardSalesActivity) activity).findViewById(R.id.address)).getText().toString());
                        if (location == null) {
                            ((TextView) ((SearchYardSalesActivity) activity).findViewById(R.id.errMessage)).setText("The address does not exist");
                            latitude = null;
                            longtitude = null;
                        } else {
                            latitude = String.valueOf(location.latitude);
                            longtitude = String.valueOf(location.longitude);
                        }
                    } else {
                        ((TextView) ((SearchYardSalesActivity) activity).findViewById(R.id.errMessage)).setText("Please enter the address.");
                        latitude = null;
                        longtitude = null;
                    }
                }
            }
        }
        else {
            ((TextView) ((SearchYardSalesActivity) activity).findViewById(R.id.errMessage)).setText("Please check internet connection.");
        }
    }
    /*
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
    */
}

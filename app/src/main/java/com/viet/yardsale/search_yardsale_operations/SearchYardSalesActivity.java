package com.viet.yardsale.search_yardsale_operations;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.viet.yardsale.R;
import com.viet.yardsale.android_php_yardsale.GetAvailableYardSalesInformation;
import com.viet.yardsale.services.LocatingOperations;
import com.viet.yardsale.services.StaticComponents;


public class SearchYardSalesActivity extends Activity {

    public LocatingOperations ls;
    public boolean useCurrentLocation = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_yardsales);
        ls = new LocatingOperations(this);

        if(StaticComponents.showAds){
            AdView adView = (AdView)this.findViewById(R.id.loginAd);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("TEST_DEVICE_ID")
                    .build();
            adView.loadAd(adRequest);
            findViewById(R.id.adLayout).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_give_adrive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void useCurrentLocation(View view){
        if(((CheckBox)findViewById(R.id.currentLocation)).isChecked()){
            findViewById(R.id.address).setEnabled(false);
            useCurrentLocation = true;
        }
        else {
            findViewById(R.id.address).setEnabled(true);
            useCurrentLocation = false;
        }
    }

    public void submit(View view){

        ls.updateLocation();
        if (ls.latitude != null && ls.longtitude != null) {
            StaticComponents.latitude = Double.parseDouble(ls.latitude);
            StaticComponents.longtitude = Double.parseDouble(ls.longtitude);
            GetAvailableYardSalesInformation connector = new GetAvailableYardSalesInformation(this);
            connector.execute(ls.latitude, ls.longtitude, StaticComponents.currentUser, ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString());
            StaticComponents.freezeActivity(this);//suspending the activity
        }
    }
}

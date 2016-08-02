package com.viet.yardsale.search_yardsale_operations;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.viet.yardsale.R;
import com.viet.yardsale.android_php_yardsale.GetYardSalePhotos;
import com.viet.yardsale.android_php_yardsale.GetYardSaleMainDescription;
import com.viet.yardsale.android_php_yardsale.ReportAYardSale;
import com.viet.yardsale.services.StaticComponents;

public class YardSalePreviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yardsale_preview);

        GetYardSaleMainDescription gmd = new GetYardSaleMainDescription(this);
        gmd.execute(StaticComponents.owner_of_yard_sale);

        GetYardSalePhotos dp = new GetYardSalePhotos(this);
        dp.execute();


        StaticComponents.freezeActivity(this);

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
        getMenuInflater().inflate(R.menu.menu_yard_sale_preview, menu);
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

    public void reportYardSale(View view){
        ReportAYardSale rays = new ReportAYardSale(this);
        rays.execute(StaticComponents.currentUser, StaticComponents.owner_of_yard_sale);
        StaticComponents.freezeActivity(this);
    }

    public void directToYardSale(View view){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?daddr="+StaticComponents.looking_at_yard_sale_latitude+","+StaticComponents.looking_at_yard_sale_longtitude));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");//turn of the dialog suggesting some apps to do navigation.
        startActivity(intent);
    }
}

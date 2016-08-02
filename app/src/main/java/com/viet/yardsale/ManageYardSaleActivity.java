package com.viet.yardsale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.viet.yardsale.android_php.yardsale.ClearUserYardSale;
import com.viet.yardsale.android_php.yardsale.IfUserPostYardSale;
import com.viet.yardsale.services.StaticComponents;


public class ManageYardSaleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_yardsale);

        if(StaticComponents.showAds){
            AdView adView = (AdView)this.findViewById(R.id.loginAd);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("TEST_DEVICE_ID")
                    .build();
            adView.loadAd(adRequest);
            findViewById(R.id.adLayout).setVisibility(View.VISIBLE);
        }
        setupForUserHasYardSale();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_yard_sale, menu);
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

    public void setupForUserHasYardSale(){
        IfUserPostYardSale uhys = new IfUserPostYardSale(this);
        uhys.execute(StaticComponents.currentUser);
        StaticComponents.freezeActivity(this);
    }

    public void clearUserYardSale(View view){
            ClearUserYardSale clearUYS = new ClearUserYardSale(this);
            clearUYS.execute(StaticComponents.currentUser);
            StaticComponents.freezeActivity(this);
    }

    public void renewMe(View view){
        Intent intent = new Intent(this, ManageYardSaleActivity.class);
        startActivity(intent);
        finish();
    }

    public void returnToMainActivity(View view){
        finish();
    }
}

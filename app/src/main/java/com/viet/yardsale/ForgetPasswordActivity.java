package com.viet.yardsale;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.viet.yardsale.android_php_yardsale.GenerateUserTempPassword;
import com.viet.yardsale.services.StaticComponents;


public class ForgetPasswordActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
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
        getMenuInflater().inflate(R.menu.menu_forget_password, menu);
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

    public void generateTempPW(View view){
        String temp = ((EditText)findViewById(R.id.userName)).getText().toString();
        String temp1 = ((EditText)findViewById(R.id.registeredEmail)).getText().toString();
        if(temp.length() > 5 && temp.length() < 21) {
            if(StaticComponents.isValidEmail(temp1)) {
                GenerateUserTempPassword gtp = new GenerateUserTempPassword(this);
                gtp.execute(temp,temp1);
                StaticComponents.freezeActivity(this);
            }
            else {
                ((TextView) findViewById(R.id.errMessage)).setText("Please check your email");
            }
        }
        else {
            ((TextView) findViewById(R.id.errMessage)).setText("Please check your user name");
        }
    }
}

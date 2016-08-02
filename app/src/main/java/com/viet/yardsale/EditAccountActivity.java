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
import com.viet.yardsale.android_php_yardsale.ChangeUserEmail;
import com.viet.yardsale.android_php_yardsale.ChangeUserPassword;
import com.viet.yardsale.android_php_yardsale.GetCurrentUserEmail;
import com.viet.yardsale.services.StaticComponents;


public class EditAccountActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        GetCurrentUserEmail gcue = new GetCurrentUserEmail(this);
        gcue.execute(StaticComponents.currentUser);
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
        getMenuInflater().inflate(R.menu.menu_edit_account, menu);
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

    public void updatePassword(View view){
        String temp1 = ((EditText)findViewById(R.id.currentPW)).getText().toString();
        String temp2 = ((EditText)findViewById(R.id.newPW)).getText().toString();
        String temp3 = ((EditText)findViewById(R.id.confirmNewPW)).getText().toString();
        if(temp1.length() > 5 && temp1.length() < 21) {
            if (temp2.length() > 5 && temp2.length() < 21) {
                if (temp2.equals(temp3)) {
                    ChangeUserPassword cup = new ChangeUserPassword(this);
                    cup.execute(StaticComponents.currentUser, temp1, temp2);
                    StaticComponents.freezeActivity(this);
                } else {
                    ((TextView) findViewById(R.id.errMessage)).setText("New password and confirm new password do not match.");
                }
            }
            else {
                ((TextView) findViewById(R.id.errMessage)).setText("New password's length must be between 5 and 21");
            }
        }
        else {
            ((TextView) findViewById(R.id.errMessage)).setText("Please check your current password");
        }
    }

    public void updateEmail(View view){
        String temp1 = ((EditText)findViewById(R.id.newEmail)).getText().toString();
        if(StaticComponents.isValidEmail(temp1)) {
            ChangeUserEmail cue = new ChangeUserEmail(this);
            cue.execute(StaticComponents.currentUser, temp1);
            StaticComponents.freezeActivity(this);
        }
        else {
            ((TextView) findViewById(R.id.errMessage)).setText("Invalid email address");
        }
    }
}

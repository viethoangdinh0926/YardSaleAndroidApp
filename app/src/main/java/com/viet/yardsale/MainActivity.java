package com.viet.yardsale;

import com.google.android.gms.ads.*;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.viet.yardsale.post_yardsale_operations.PostYardSaleMainActivity;
import com.viet.yardsale.search_yardsale_operations.SearchYardSalesActivity;
import com.viet.yardsale.android_php.yardsale.CheckAppVersion;
import com.viet.yardsale.android_php.yardsale.SignupLoginOperations;
import com.viet.yardsale.services.StaticComponents;
import com.viet.yardsale.services.UserPhoneServices;


public class MainActivity extends Activity{

    public int isNewestVersion = 0;//0: no internet connection, 1: newest version, 2:old version

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView adView = (AdView)this.findViewById(R.id.loginAd);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("TEST_DEVICE_ID")
                .build();
        adView.loadAd(adRequest);

        initApp();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void logIn(View view){
        if(UserPhoneServices.isInternetConnectionOn(this)) {
            SignupLoginOperations mySQLAndroidConnector = new SignupLoginOperations(this, 0);
            mySQLAndroidConnector.execute(((EditText) findViewById(R.id.userName)).getText().toString(), ((EditText) findViewById(R.id.password)).getText().toString());
            StaticComponents.freezeActivity(this);
        }
        else {
            findViewById(R.id.bLoginLayout).setVisibility(View.INVISIBLE);
            findViewById(R.id.checkInternetLayout).setVisibility(View.VISIBLE);
        }
    }


    public void signUp(View view){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    public void signOut(View view){
        findViewById(R.id.bLoginLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.aLoginLayout).setVisibility(View.INVISIBLE);
        StaticComponents.currentUser = null;
    }

    public void postYardSale(View view){
        if(UserPhoneServices.isInternetConnectionOn(this)) {
            Intent intent = new Intent(this, PostYardSaleMainActivity.class);
            startActivity(intent);
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.VISIBLE);
        }
    }

    public void searchYardSales(View view){
        if(UserPhoneServices.isInternetConnectionOn(this)) {
            Intent intent = new Intent(this, SearchYardSalesActivity.class);
            startActivity(intent);
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.INVISIBLE);
        }
        else{
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.VISIBLE);
        }
    }


    public void initApp(){
        if(!UserPhoneServices.isInternetConnectionOn(this)){//no internet connectin
            findViewById(R.id.checkInternetLayout).setVisibility(View.VISIBLE);
        }
        else {
            if(StaticComponents.alreadyCheckAppVersion == false) {
                CheckAppVersion cv = new CheckAppVersion(this);
                try {
                    cv.execute(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                    StaticComponents.freezeActivity(this);//make the activity disable until processing finish
                } catch (PackageManager.NameNotFoundException e) {
                    System.exit(0);//close app
                }
            }
            else if(StaticComponents.currentUser == null){//there is no a user logged in system
                findViewById(R.id.bLoginLayout).setVisibility(View.VISIBLE);
                if(StaticComponents.showAds){
                    findViewById(R.id.adLayout).setVisibility(View.VISIBLE);
                }
            }
            else {
                findViewById(R.id.aLoginLayout).setVisibility(View.VISIBLE);
                if(StaticComponents.showAds){
                    findViewById(R.id.adLayout).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void tryAgainBtt(View view){//try to setup UI after recovering internet connection
        if(UserPhoneServices.isInternetConnectionOn(this)){
            findViewById(R.id.checkInternetLayout).setVisibility(View.INVISIBLE);//hide this layout

            if(StaticComponents.alreadyCheckAppVersion == false) {
                CheckAppVersion cv = new CheckAppVersion(this);
                try {
                    cv.execute(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
                    StaticComponents.freezeActivity(this);//make the activity disable until processing finish
                } catch (PackageManager.NameNotFoundException e) {
                    System.exit(0);//close app
                }
            }
            else if(StaticComponents.currentUser == null){//there is no a user logged in system
                findViewById(R.id.bLoginLayout).setVisibility(View.VISIBLE);
            }
            else {//there is a user logged in system
                findViewById(R.id.aLoginLayout).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        StaticComponents.currentUser = null;
        StaticComponents.alreadyCheckAppVersion = false;
        System.exit(0);
    }

    public void closeApp(View view){
        StaticComponents.currentUser = null;
        StaticComponents.alreadyCheckAppVersion = false;
        System.exit(0);
    }

    public void openForgetPWActivity(View view){
        if(UserPhoneServices.isInternetConnectionOn(this)) {
            Intent intent = new Intent(this, ForgetPasswordActivity.class);
            startActivity(intent);
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.VISIBLE);
        }

    }

    public void openEditAccountInfoActivity(View view){
        if(UserPhoneServices.isInternetConnectionOn(this)) {
            Intent intent = new Intent(this, EditAccountActivity.class);
            startActivity(intent);
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.VISIBLE);
        }
    }

    public void manageYardSale(View view){
        if(UserPhoneServices.isInternetConnectionOn(this)) {
            Intent intent = new Intent(this, ManageYardSaleActivity.class);
            startActivity(intent);
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.INVISIBLE);
        }
        else {
            findViewById(R.id.aLoginErrorLayout).setVisibility(View.VISIBLE);
        }

    }

    public void bringUsertoUpdate(View view){
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("market://details?id=com.viet.yardsale"));
        startActivity(intent);
    }


}

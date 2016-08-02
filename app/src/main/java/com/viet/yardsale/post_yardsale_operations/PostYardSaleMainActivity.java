package com.viet.yardsale.post_yardsale_operations;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.viet.yardsale.R;
import com.viet.yardsale.android_php_yardsale.PostYardSaleInformation;
import com.viet.yardsale.services.LocatingOperations;
import com.viet.yardsale.services.StaticComponents;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PostYardSaleMainActivity extends Activity {
    public LocatingOperations ls;
    public boolean useCurrentLocation = false;
    public Date[] dates = new Date[7];
    public String[] formattedDates = new String[7];
    public long pickedDate;

    private int showEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_yardsale);

        ls = new LocatingOperations(this);
        showEmail = 0;
        setDateList();

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
        getMenuInflater().inflate(R.menu.menu_need_adrive, menu);
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

    public void onResume(){
        super.onResume();
        setDateList();
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

    public void setShowEmail(View view){
        if(((CheckBox)findViewById(R.id.showEmail)).isChecked()){
            showEmail = 1;
        }
        else {
            showEmail = 0;
        }
    }

    public void submit(View view){
        PostYardSaleInformation pysi = new PostYardSaleInformation(this);
        ls.updateLocation();
        if(ls.latitude != null && ls.longtitude != null) {
            pysi.execute(ls.latitude, ls.longtitude, ((EditText) findViewById(R.id.request)).getText().toString(), ((EditText) findViewById(R.id.phone)).getText().toString(), StaticComponents.currentUser, String.valueOf(pickedDate), ((Spinner) findViewById(R.id.openHrs)).getSelectedItem().toString(), String.valueOf(showEmail));
            StaticComponents.freezeActivity(this); //suspending the activity here to wait for processing users data
        }
    }

    /**
     * Create a list of dates that users can choose from to open yard sales
     */
    public void setDateList(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("EEEE MM/dd/yyyy");

        for(int i = 1; i < 8; i++){
            c.setTime(c.getTime());
            c.add(Calendar.DATE, 1);
            dates[i-1] = c.getTime();
            formattedDates[i-1] = df.format(dates[i-1].getTime());
        }
        ArrayAdapter<String> adp1=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,formattedDates);
        pickedDate = Math.round(dates[0].getTime()/60000);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner) findViewById(R.id.sp1)).setAdapter(adp1);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i <= 7; i++) {
                    if (((Spinner) findViewById(R.id.sp1)).getSelectedItem().toString().equals(formattedDates[i])) {
                        String temp = ((Spinner) findViewById(R.id.closeHrs)).getSelectedItem().toString();
                        int timeofOpenDay = (Integer.valueOf(temp.substring(0, temp.indexOf("PM") - 1)) + 12) * 60;//in minute
                        int pickedDate_in_minute = (int) (Math.round(dates[i].getTime() / 60000));
                        int minute_remainder = pickedDate_in_minute % 1440;
                        pickedDate = (pickedDate_in_minute - minute_remainder) + timeofOpenDay;
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        ((Spinner) findViewById(R.id.sp1)).setOnItemSelectedListener(listener);
        ((Spinner) findViewById(R.id.closeHrs)).setOnItemSelectedListener(listener);
    }
}

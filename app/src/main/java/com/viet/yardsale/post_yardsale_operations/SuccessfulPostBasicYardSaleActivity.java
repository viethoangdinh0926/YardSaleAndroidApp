package com.viet.yardsale.post_yardsale_operations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.viet.yardsale.R;


public class SuccessfulPostBasicYardSaleActivity extends Activity {
    private Camera mCamera;
    final private String TAG = "FirstImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_post_yardsale);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_successful_post, menu);
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

    public void finishActivity(View view){
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void startAddDetails(View view){
        if(checkCameraHardware(this)){
            mCamera = Camera.open(0);
            if(mCamera != null) {
                mCamera.stopPreview();
                mCamera.release();
                Intent intent = new Intent(this, AddingMoreYardSaleDetailActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                ((TextView)findViewById(R.id.err)).setText("camera is unavailable");
            }
        }
        else {
            ((TextView)findViewById(R.id.err)).setText("no camera found");
        }
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

}

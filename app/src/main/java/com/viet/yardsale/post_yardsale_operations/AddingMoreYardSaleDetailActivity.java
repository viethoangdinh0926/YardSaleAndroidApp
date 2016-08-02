package com.viet.yardsale.post_yardsale_operations;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.viet.yardsale.R;
import com.viet.yardsale.android_php.yardsale.AddDescriptionToYardSalePhoto;
import com.viet.yardsale.android_php.yardsale.AddPhotoToYardSale;
import com.viet.yardsale.services.StaticComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddingMoreYardSaleDetailActivity extends Activity implements SensorEventListener{
    public Bitmap currentPicBitmap;
    private static String TAG = "Camera";
    private CameraPreview cameraPreview;
    private Camera mCamera;
    private SensorManager sensorManager;
    private float gravityX;
    private float gravityY;
    private float gravityZ;
    private static Activity me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_yardsale_detail);

        me = this;
        mCamera = Camera.open(0);
        cameraPreview = new CameraPreview(this, mCamera);
        cameraPreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    focusOnTouch(event);
                }
                return true;
            }
        });

        ((FrameLayout)findViewById(R.id.cameraFrame)).addView(cameraPreview);


        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        // add listener. The listener will be  (this) class
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);

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
        getMenuInflater().inflate(R.menu.menu_products, menu);
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

    public void onDestroy(){
        super.onDestroy();
        StaticComponents.uploadedImageCount = 0;
    }

    public void onPause(){
        super.onPause();
        finish();
    }

    public void onFinish(View view){
        if(!((EditText)findViewById(R.id.productDescription)).getText().toString().equals("")) {
            AddDescriptionToYardSalePhoto upd = new AddDescriptionToYardSalePhoto(this, true); // "true" mean close the activity
            upd.execute("user/yardsale/" + StaticComponents.currentUser + "/" + StaticComponents.currentUser + StaticComponents.uploadedImageCount + ".jpg", ((EditText) findViewById(R.id.productDescription)).getText().toString());
        }
        else{
            finish();
        }
    }

    public void onNext(View view){
        if(!((EditText)findViewById(R.id.productDescription)).getText().toString().equals("")) {
            AddDescriptionToYardSalePhoto upd = new AddDescriptionToYardSalePhoto(this, false); // "false" mean taking next product picture
            upd.execute("user/yardsale/" + StaticComponents.currentUser + "/" + StaticComponents.currentUser + StaticComponents.uploadedImageCount + ".jpg", ((EditText) findViewById(R.id.productDescription)).getText().toString());
            StaticComponents.freezeActivity(this);
        }
        else {
            findViewById(R.id.cameraFrame).setVisibility(View.VISIBLE);
            findViewById(R.id.bttLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.imgPreviewLayout).setVisibility(View.INVISIBLE);
            findViewById(R.id.descripeProductLayout).setVisibility(View.INVISIBLE);
        }
        StaticComponents.uploadedImageCount++;
    }



    private void focusOnTouch(MotionEvent event) {
        if (mCamera != null ) {

            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0){
                Log.i(TAG, "fancy !");
                Rect rect = calculateFocusArea(event.getX(), event.getY());

                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);

                mCamera.setParameters(parameters);
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (camera.getParameters().getFocusMode() != Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
                            Camera.Parameters parameters = camera.getParameters();
                            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                            if (parameters.getMaxNumFocusAreas() > 0) {
                                parameters.setFocusAreas(null);
                            }
                            camera.setParameters(parameters);
                            camera.startPreview();
                        }
                    }
                });
            }else {
                mCamera.autoFocus(new Camera.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (camera.getParameters().getFocusMode() != Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) {
                            Camera.Parameters parameters = camera.getParameters();
                            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                            if (parameters.getMaxNumFocusAreas() > 0) {
                                parameters.setFocusAreas(null);
                            }
                            camera.setParameters(parameters);
                            camera.startPreview();
                        }
                    }
                });
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / cameraPreview.getWidth()) * 2000 - 1000).intValue(), 300);
        int top = clamp(Float.valueOf((y / cameraPreview.getHeight()) * 2000 - 1000).intValue(), 300);

        return new Rect(left, top, left + 300, top + 300);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper)+focusAreaSize/2>1000){
            if (touchCoordinateInCameraReper>0){
                result = 1000 - focusAreaSize/2;
            } else {
                result = -1000 + focusAreaSize/2;
            }
        } else{
            result = touchCoordinateInCameraReper - focusAreaSize/2;
        }
        return result;
    }

    public void takePic(View view){
        Camera.PictureCallback mCall = new Camera.PictureCallback()
        {
            public void onPictureTaken(final byte[] data, Camera camera)
            {
                FileOutputStream outStream = null;
                try{
                    File image = new File(getCacheDir(), StaticComponents.currentUser + StaticComponents.uploadedImageCount + ".jpg");
                    if(image.exists()) {
                        image.delete();
                    }
                    image.createNewFile();
                    outStream = new FileOutputStream(image);
                    outStream.write(data);
                    outStream.flush();
                    outStream.close();

                    // adjust bitmap size for showing
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    BitmapFactory.decodeStream(new FileInputStream(image), null, bmOptions);
                    int scale = 1;
                    while(bmOptions.outWidth / scale > 2000 || bmOptions.outHeight / scale > 2000) {
                        scale *= 2;
                    }
                    BitmapFactory.Options bmOptions2 = new BitmapFactory.Options();
                    bmOptions2.inSampleSize = scale;
                    currentPicBitmap = BitmapFactory.decodeStream(new FileInputStream(image), null, bmOptions2);

                    //rotate bitmap here...
                    currentPicBitmap = rotateBitmap(currentPicBitmap, gravityX, gravityY, gravityZ);

                    ((ImageView)findViewById(R.id.imageView)).setImageBitmap(currentPicBitmap);
                    findViewById(R.id.cameraFrame).setVisibility(View.INVISIBLE);
                    findViewById(R.id.bttLayout).setVisibility(View.INVISIBLE);
                    findViewById(R.id.imgPreviewLayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.descripeProductLayout).setVisibility(View.INVISIBLE);

                    StaticComponents.unfreezeActivity(me);//release the activity
                    me = null;

                } catch (FileNotFoundException e){
                    Log.d("CAM", e.getMessage());
                } catch (IOException e){
                    Log.d("CAM", e.getMessage());
                }}
        };
        mCamera.takePicture(null, null, mCall);

        StaticComponents.freezeActivity(this);//suspending the activity

    }

    public void reTakePic(View view){
        File image = new File(getCacheDir(), StaticComponents.currentUser + StaticComponents.uploadedImageCount + ".jpg");
        image.delete();

        findViewById(R.id.cameraFrame).setVisibility(View.VISIBLE);
        findViewById(R.id.bttLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.imgPreviewLayout).setVisibility(View.INVISIBLE);
        findViewById(R.id.descripeProductLayout).setVisibility(View.INVISIBLE);
    }

    public void uploadPic(View view){
        AddPhotoToYardSale upp = new AddPhotoToYardSale(this);
        upp.execute();
        StaticComponents.freezeActivity(this);//suspending the activity while waiting for uploading picture to the database
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        gravityX = event.values[0];
        gravityY = event.values[1];
        gravityZ = event.values[2];

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public Bitmap rotateBitmap(Bitmap bitmap, float x, float y, float z){

        if (y > 5) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else if (y < -5) {
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } else if(x < 0){
            Matrix matrix = new Matrix();
            matrix.postRotate(-180);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }
}

package com.viet.yardsale.android_php.yardsale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.viet.yardsale.R;
import com.viet.yardsale.post_yardsale_operations.AddingMoreYardSaleDetailActivity;
import com.viet.yardsale.services.StaticComponents;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Viet on 6/12/2015.
 */
public class AddPhotoToYardSale extends AsyncTask {
    private Activity activity;
    private File image;
    public AddPhotoToYardSale(Activity activity) {
        this.activity = activity;
        image = new File(activity.getCacheDir(), StaticComponents.currentUser + StaticComponents.uploadedImageCount + ".jpg");
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            //Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), options);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ((AddingMoreYardSaleDetailActivity)activity).currentPicBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] byte_arr = out.toByteArray();
            String image_str = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("image", image_str));
            nameValuePairs.add(new BasicNameValuePair("username", StaticComponents.currentUser));
            nameValuePairs.add(new BasicNameValuePair("fileNum", String.valueOf(StaticComponents.uploadedImageCount)));

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(StaticComponents.dbAdress + "uploadPicture.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        }
        catch (IOException ex){
            return ex.toString();
        }

    }

    @Override
    protected void onPostExecute(Object result) {

        StaticComponents.unfreezeActivity(activity);//release the activity after finish uploading

        AddingMoreYardSaleDetailActivity pa = (AddingMoreYardSaleDetailActivity)activity;
        String temp = (String)result;
        if(temp.equals("ok")) {
            image.delete();

            if(StaticComponents.uploadedImageCount > 1){
                pa.findViewById(R.id.next).setVisibility(View.INVISIBLE);
            }

            pa.findViewById(R.id.cameraFrame).setVisibility(View.INVISIBLE);
            pa.findViewById(R.id.bttLayout).setVisibility(View.INVISIBLE);
            pa.findViewById(R.id.imgPreviewLayout).setVisibility(View.INVISIBLE);
            pa.findViewById(R.id.descripeProductLayout).setVisibility(View.VISIBLE);
            ((TextView)pa.findViewById(R.id.errUpload)).setText("");
        }
        else {
            ((TextView)pa.findViewById(R.id.errUpload)).setText((String)result);
        }
    }
}
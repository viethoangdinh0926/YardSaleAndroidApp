package com.viet.yardsale.android_php.yardsale;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import com.viet.yardsale.R;
import com.viet.yardsale.search_yardsale_operations.YardSalePreviewActivity;
import com.viet.yardsale.services.StaticComponents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Viet on 6/23/2015.
 */
public class GetYardSalePhotos extends AsyncTask {
    private Activity activity;
    private File[] images;

    public GetYardSalePhotos(Activity activity) {
        this.activity = activity;
        images = new File[3];
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            for(int i = 0; i < Integer.valueOf(StaticComponents.number_of_yard_sale_photos); i++) {
                images[i] = new File(activity.getCacheDir(),"image"+i+".jpg");
                URL u = new URL(StaticComponents.dbAdress + "user/yardsale/"+StaticComponents.owner_of_yard_sale+"/"+StaticComponents.owner_of_yard_sale+i+".jpg");
                URLConnection conn = u.openConnection();
                int contentLength = conn.getContentLength();

                DataInputStream stream = new DataInputStream(u.openStream());

                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();

                DataOutputStream fos = new DataOutputStream(new FileOutputStream(images[i]));
                fos.write(buffer);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            activity.finish();
            return "Cannot find the file.";
        } catch (IOException e) {
            activity.finish();
            return "writting file error!";
        }
        return "ok";
    }


    @Override
    protected void onPostExecute(Object result) {
        StaticComponents.unfreezeActivity(activity);
        if(((String)result).equals("ok")) {
            for(int i = 0; i < images.length; i++){
                if(images[i] != null){
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(images[i].getAbsolutePath(), options);
                    if(i == 0){
                        ((ImageView)((YardSalePreviewActivity)activity).findViewById(R.id.pic1)).setImageBitmap(bitmap);
                        ((YardSalePreviewActivity)activity).findViewById(R.id.pic1Layout).setVisibility(View.VISIBLE);
                    }
                    else if(i == 1){
                        ((ImageView)((YardSalePreviewActivity)activity).findViewById(R.id.pic2)).setImageBitmap(bitmap);
                        ((YardSalePreviewActivity)activity).findViewById(R.id.pic2Layout).setVisibility(View.VISIBLE);
                    }
                    else if(i == 2){
                        ((ImageView)((YardSalePreviewActivity)activity).findViewById(R.id.pic3)).setImageBitmap(bitmap);
                        ((YardSalePreviewActivity)activity).findViewById(R.id.pic3Layout).setVisibility(View.VISIBLE);
                    }
                    images[i].delete();

                    GetPictureDescription gpd = new GetPictureDescription(activity);
                    gpd.execute(StaticComponents.owner_of_yard_sale, String.valueOf(i));
                }
            }
            activity.findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
        }
    }
}